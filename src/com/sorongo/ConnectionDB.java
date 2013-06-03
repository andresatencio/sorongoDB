package com.sorongo;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import android.util.Log;


public class ConnectionDB extends CordovaPlugin{
	
	private JSONArray json;
	private Connection conn;
    private Statement stmt;
    
    
    private void conectate(String IP, String PUERTO, String DB, String USER, String PASS) throws Exception {
    	

            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@"+IP+":"+PUERTO+":"+DB;
            this.conn = DriverManager.getConnection(url,USER,PASS);
            this.stmt = this.conn.createStatement();
            
       
    }
    private ResultSet getResult(String QUERY) throws SQLException {
        ResultSet rset = stmt.executeQuery(QUERY);
        stmt.close();
        return rset;            
    }
	
	
	@Override
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
	    System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
	    //System.out.println(data.toString());
		if ("CONECTAR".equals(action)) {
	    	try{
		    	this.conectate(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4));
		    	callbackContext.success("Conectado OK");
	    	}catch(Exception e){
	    		callbackContext.success("Error en la conexion");
	    		e.printStackTrace();
	    	}
	        return true;
	    }
		if("CONSULTA".equals(action)){
			try {
				String msj = this.getResult(data.getString(0)).toString();
				callbackContext.success(msj);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callbackContext.success("ERROR");
			}
	    	
			return true;
		}
		if("SORONGO".equals(action)){
			callbackContext.success("{SORONGO:SORONGO}");
			return true;
		}
	    return false;  // Returning false results in a "MethodNotFound" error.
	}
	
	
	
	
	
	
	public static JSONArray convert( ResultSet rs ) throws SQLException, JSONException{
	    
		JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();

	    while(rs.next()) {
	      int numColumns = rsmd.getColumnCount();
	      JSONObject obj = new JSONObject();

	      for (int i=1; i<numColumns+1; i++) {
	        String column_name = rsmd.getColumnName(i);

	        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	         obj.put(column_name, rs.getArray(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	         obj.put(column_name, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	         obj.put(column_name, rs.getBoolean(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	         obj.put(column_name, rs.getBlob(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	         obj.put(column_name, rs.getDouble(column_name)); 
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	         obj.put(column_name, rs.getFloat(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	         obj.put(column_name, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	         obj.put(column_name, rs.getString(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	         obj.put(column_name, rs.getString(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	         obj.put(column_name, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	         obj.put(column_name, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	         obj.put(column_name, rs.getDate(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	        obj.put(column_name, rs.getTimestamp(column_name));   
	        }
	        else{
	         obj.put(column_name, rs.getObject(column_name));
	        }
	      }

	      json.put(obj);
	    }

	    return json;
	  }
}
