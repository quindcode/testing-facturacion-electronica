const { defineConfig } = require("cypress");
const oracledb = require("oracledb");
require("dotenv").config();
const { beforeRunHook, afterRunHook } = require('cypress-mochawesome-reporter/lib');
const { getTemporaryCredentials } = require('./cypress/support/utils/AWSConnection.js');


// Establece la configuración de conexión para el ambiente de base de datos
const connection = {
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  connectString: process.env.DB_CONNECT_STRING,
};

// Establece la función para realizar consultas a la base de datos Oracle
// Establece la función para realizar consultas a la base de datos Oracle
function queryDB(query) {
  return new Promise((resolve, reject) => {
    oracledb.getConnection(connection, (error, connection) => {
      if (error) {
        reject(error);
      } else {
        connection.execute(query, (error, result) => {
          connection.close(() => {
            if (error) {
              reject(error);
            } else {
              if (result.rows) {
                // Procesa el resultado para devolverlo como clave-valor (para consultas SELECT)
                const metaData = result.metaData || [];
                const processedResult = result.rows.map((row) => {
                  const obj = {};
                  metaData.forEach((column, index) => {
                    obj[column.name] = row[index];
                  });
                  return obj;
                });
                resolve(processedResult);
              } else {
                // Devuelve información sobre la cantidad de filas afectadas (para UPDATE y DELETE)
                resolve({
                  rowsAffected: result.rowsAffected,
                });
              }
            }
          });
        });
      }
    });
  });
}

module.exports = defineConfig({
  // Ajusta el tiempo de espera predeterminado en milisegundos
  defaultCommandTimeout: 5000,
  pageLoadTimeout: 10000,
  reporter: 'cypress-mochawesome-reporter',
  reporterOptions: {
    charts: true,
    reportPageTitle: 'custom-title',
    embeddedScreenshots: true,
    inlineAssets: true,
    saveAllAttempts: false,
  },
  e2e: {
    // Al iniciar la prueba esta será la url base
    baseUrl: "https://reqres.in/api",
    setupNodeEvents(on, config) {
      require('cypress-mochawesome-reporter/plugin')(on);
      on("task", {
        queryDatabase({ query }) {
          return queryDB(query);
        },
        async awsSetTemporaryCredentials() {
          const awsCredentials = {
            accessKeyId: process.env.AWS_ACCESS_KEY_ID, 
            secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
            sessionToken: process.env.AWS_SESSION_TOKEN,
            region: process.env.AWS_REGION
          };

          const credentials = await getTemporaryCredentials(awsCredentials);
          return credentials;
        }
      });
      on('before:run', async (details) => {
        console.log('override before:run');
        await beforeRunHook(details);
      });
      on('after:run', async () => {
        console.log('override after:run');
        await afterRunHook();
      });
    },
  },
});