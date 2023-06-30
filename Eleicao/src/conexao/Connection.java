package conexao;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

@SuppressWarnings("deprecation")
public class Connection {
	DB BaseDados;
	DBCollection colecao;
	BasicDBObject document = new BasicDBObject ();
 
	public Connection() {
		try {
			//Conectando ao servidor mongodb
			Mongo m = new Mongo ("localhost", 27017);
		
			//Conectando a base eleicao
			BaseDados = m.getDB("eleicao");
		
			colecao = BaseDados.getCollection("nome");
		
			System.out.println("Conexao efetuada com sucesso!");
		} catch(UnknownHostException e) {
			Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public boolean inserir(String Dado) {
		document.put("nome", Dado);
		colecao.insert(document);
		return true;
		
	}
}