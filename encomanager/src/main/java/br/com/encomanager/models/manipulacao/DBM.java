package br.com.encomanager.models.manipulacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.encomanager.util.Registro;
import br.com.encomanager.util.StringUtils;

/**
 * 
 * Classe utilizada para executar opera��es no banco de dados. (Data Base Manipulation).
 *
 */
public class DBM {
	//Atributos
	private Statement stm = null;
	
	//Construtores
	public DBM(Connection con) {
		try {
			Statement stm = con.createStatement();
			this.stm = stm;
			
		} catch (Exception e) {
			System.out.println("Erro ao criar Statement!");
			e.printStackTrace();
		}  
	}
	
	public DBM(Statement stm) {
		this.stm = stm;
	}
	
	//Met�dos publicos
	
	//DQL functions
	
	/**
	 * 
	 * @param tableName
	 * Nome da tabela alvo
	 * 
	 * @param requiredColumns
	 * Colunas que ser�o returnadas Ex: "column1, column2"
	 * 
	 * @return List<?>
	 */
	public List<Registro> select(String tableName, String requiredColumns) {
		try {                
	        //Prepara a string do SELECT
	        String sql = "SELECT " + requiredColumns + " FROM " + tableName;	       
	        
	        List<Registro> recordsList = getRecordsList(sql);	        
	        
	        return recordsList;
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar select");
			return null;
		}
	}
		
	/**
	 * 
	 * @param tableName
	 * Nome da tabela alvo
	 * 
	 * @param requiredColumns
	 * Colunas que ser�o returnadas Ex: "column1, column2"
	 * 
	 * @param whereQuery
	 * Clausula where Ex: "column = 123"
	 * 
	 * @return List<?>
	 */
	public List<Registro> select(String tableName, String requiredColumns, String whereQuery) {
		try {                
	        //Prepara a string do SELECT
	        String sql = "SELECT " + requiredColumns + " FROM " + tableName + " WHERE " + whereQuery;	       
	        
	        List<Registro> recordsList = getRecordsList(sql);	        
	        
	        return recordsList;
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar select");
			return null;
		}
	}
	
	/**
	 * 
	 * @param query
	 * String com a query que aceita par�metros Ex: "select * from table where column = ?"
	 * 
	 * @param parameters
	 * Array de objetos que possa ser convertido pra string, que contem os valores dos par�metros respectivamente
	 * 
	 * @return List<?>
	 */
	public List<Registro> select(String query, Object[] parameters) {
		try {                    
			String sqlFinal = StringUtils.stringWithParams(query, parameters);
	        
	        List<Registro> recordsList = getRecordsList(sqlFinal);	        
	        
	        return recordsList;
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar select");
			return null;
		}
	}
	
	//DML functions	
	
	/**
	 * 
	 * @param tableName
	 * Nome da tabela alvo
	 * 
	 * @param requiredColumns
	 * Colunas que ter�o o seu valor setado Ex: "column1, column2"
	 * 
	 * @param stringValues
	 * Valores das colunas passadas no par�metro "requiredColumns" Ex: "1,'teste',null"
	 */
	public void insert(String tableName, String requiredColumns, String stringValues) {
		try {                
	        //Prepara a string do INSERT
	        String sql = "INSERT INTO " + tableName + "("+ requiredColumns +") " + "VALUES(" + stringValues + ")";
	        
	        //Executa INSERT  
	        this.stm.executeUpdate(sql);
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar insert");
		} 
	}
	
	/**
	 * 
	 * @param tableName
	 * Nome da tabela alvo
	 * 
	 * @param deleteWhereCodition
	 * Clausula "where" para remo��o de registro Ex: "column = 1"
	 */
	public void delete(String tableName, String deleteWhereCodition) {
		try {                
	        //Prepara a string do DELETE
	        String sql = "DELETE FROM " + tableName + " WHERE " + deleteWhereCodition ;
	       
	        //Executa DELETE  
	        this.stm.executeUpdate(sql);
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar delete");
		} 
	}
	
	/**
	 * 
	 * @param tableName
	 * Nome da tabela alvo
	 * 
	 * @param columnAndValueList
	 * String que contem as colunas e os valores para o update Ex: "column = 1, column2 = 'teste'"
	 * 
	 * @param updateWhereCondition
	 * 
	 */
	public void update(String tableName, String columnAndValueList, String updateWhereCondition) {
		try {                
	        //Prepara a string do UPDATE
	        String sql = "UPDATE " + tableName + " SET " + columnAndValueList + " WHERE " + updateWhereCondition;
	       
	        //Executa UPDATE  
	        this.stm.executeUpdate(sql);
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar update");
		} 
	}
	
	/**
	 * 
	 * @param query
	 * String com a query que aceita par�metros Ex: "update table set column = ? where column2 = ?"
	 * 
	 * @param parameters
	 * Array de objetos que possa ser convertido pra string, que contem os valores dos par�metros respectivamente
	 * 
	 */
	public void execute(String query, Object[] parameters) {
		try {                    
			String sqlFinal = StringUtils.stringWithParams(query, parameters);
	        
			this.stm.execute(sqlFinal);
	        
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao executar comando");
		}
	}
	
	//M�todos privados
	
	private List<Registro> getRecordsList(String sql) throws Exception{
		ResultSet rs = this.stm.executeQuery(sql);
		
		List<Registro> resultadoQuery = new ArrayList<Registro>();
		
        while(rs.next() != false) {
        	int columnsCount = rs.getMetaData().getColumnCount();
        	
        	String[] columns = new String[columnsCount];
        	Object[] values = new Object[columnsCount];
        	
        	for (int i = 1; i <= columnsCount; i++) {
        		columns[i-1] = rs.getMetaData().getColumnName(i);
        		values[i-1] = rs.getObject(i);
			}
    	
        	resultadoQuery.add(new Registro(columns, values));
        }
        
        return resultadoQuery;
	}
}
