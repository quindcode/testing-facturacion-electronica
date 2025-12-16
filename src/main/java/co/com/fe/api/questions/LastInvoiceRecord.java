package co.com.fe.api.questions;

import co.com.fe.api.abilities.UseDatabase;
import co.com.fe.api.models.dbmodels.Invoice;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;

public class LastInvoiceRecord implements Question<Invoice> {
    private final String subAccountId;
    private final String uniqueTransactionId;

    private LastInvoiceRecord(String subAccountId, String uniqueTransactionId) {
        this.subAccountId = subAccountId;
        this.uniqueTransactionId = uniqueTransactionId;
    }

    public static LastInvoiceRecord withSubAccountId(String subAccountId){
        return new LastInvoiceRecord(subAccountId, null);
    }

    public static LastInvoiceRecord withUniqueTransactionId(String uniqueTransactionId){
        return new LastInvoiceRecord(null, uniqueTransactionId);
    }

    @Override
    public Invoice answeredBy(Actor actor) {
        UseDatabase ability = actor.abilityTo(UseDatabase.class);
        if (ability == null) {
            throw new IllegalStateException("Actor does not have UseDatabase ability. Call actor.can(UseDatabase.connect()) in a Given or @Before.");
        }

        RowProcessor rowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());
        QueryRunner run = new QueryRunner();

        String query;

        if(subAccountId != null && !subAccountId.isEmpty()){
            query = "select * from toll_invoice_db.billing.invoice i where i.aggregate_root_id = ? " +
                    "order by i.created_date desc limit 1";
        }else if(uniqueTransactionId != null && !uniqueTransactionId.isEmpty()){
            query = "select * from toll_invoice_db.billing.invoice i where i.unique_transaction_id = ?;";
        }else{
            throw new RuntimeException("Se debe utilizar un atributo para consultar en base de datos");
        }

        try {
            System.out.println("Intentando obtener conexión a DB...");
            Connection conn = ability.getConnection();

            if (subAccountId != null){
                return run.query(conn, query, new BeanHandler<>(Invoice.class, rowProcessor), subAccountId);
            }else {
                return run.query(conn, query, new BeanHandler<>(Invoice.class, rowProcessor), uniqueTransactionId);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Throwable root = e;
            while (root.getCause() != null) root = root.getCause();
            System.err.println("Root cause: " + root.getClass().getName() + " : " + root.getMessage());
            throw new RuntimeException("Error al consultar la base de datos: " + e.getMessage(), e);
        }
    }
}
