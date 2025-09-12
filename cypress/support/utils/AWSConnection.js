const AWS = require('aws-sdk');

// Función para obtener las credenciales temporales
const getTemporaryCredentials = async (credentials) => {
  const sts = new AWS.STS(credentials);
  try {
    const response = await sts.getCallerIdentity().promise();
    
    return {
      accessKeyId: response.ResponseMetadata.RequestId,  //lo que devuelve getCallerIdentity
      secretAccessKey: credentials.secretAccessKey,
      sessionToken: credentials.sessionToken,
      region : "us-east-1"
    };
  } catch (error) {
    console.error('Error al obtener las credenciales temporales:', error);
    throw error;
  }
};

module.exports = { getTemporaryCredentials };