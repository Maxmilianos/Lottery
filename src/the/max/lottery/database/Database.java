package the.max.lottery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import the.max.lottery.Lottery;


public class Database {
	private static final Logger logger = Bukkit.getLogger();
    protected boolean connected = false; 
    
    public Connection c = null; 
  
    public Database() { 
    }
    
    public Connection open() {
        try { 
            Class.forName("com.mysql.jdbc.Driver"); 

            this.c = DriverManager.getConnection("jdbc:mysql://" + Lottery.getLottery()._config.getString("database.mysql.hostname") + ":3306/" + Lottery.getLottery()._config.getString("database.mysql.database") + "?user=" + Lottery.getLottery()._config.getString("database.mysql.user")  + "&password=" + Lottery.getLottery()._config.getString("database.mysql.password")); 
            return c; 
        } catch (SQLException e) { 
            System.out.println("Could not connect to Database! because: " + e.getMessage()); 
        } catch (ClassNotFoundException e) { 
            System.out.println("com.mysql.jdbc.Driver"+" not found!"); 
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
        return this.c; 
    } 
   
    public Connection getConn() { 
        return this.c; 
    } 
    public void close() {
        try {
            if(c!=null) c.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().info(ex.getMessage());
        }
        c = null; 
    } 
    public boolean isConnected() {
        try {
            return((c==null || c.isClosed()) ? false:true);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public class Result {
         private ResultSet resultSet;
         private Statement statement;

         public Result(Statement statement, ResultSet resultSet) {
             this.statement = statement;
             this.resultSet = resultSet;
         }

         public ResultSet getResultSet() {
             return this.resultSet;
         }

         public void close() {
             try {
                 this.statement.close();
                 this.resultSet.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     } 
    public Result query(final String query) {
        if (!isConnected()) open();
        return query(query,true,true);
    }
    public Result query(final String query, boolean error) {
        if (!isConnected()) open();
        return query(query,true,error);
    }
    public Result query(final String query, boolean retry, boolean error) {
        if (!isConnected()) open();
        try {
            PreparedStatement statement=null;
            try {
                if (!isConnected()) open();
                statement = c.prepareStatement(query);
                if (statement.execute())
                return new Result(statement, statement.getResultSet());
            } catch (final SQLException e) {
                final String msg = e.getMessage();
                if (error)
                	logger.severe("Database query error: " + msg);
                
                if (retry && msg.contains("_BUSY")) {
                    logger.severe("Retrying query...");
                    Bukkit.getServer().getScheduler()
                    .scheduleSyncDelayedTask(Lottery.getLottery(), new Runnable() {
                        public void run() {
                                query(query,false,error);
                            }
                        }, 20);
                }
            }
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            Bukkit.getLogger().info(ex.getMessage());
        }
        return null;
    }
    
 
    protected Statements getStatement(String query) { 
	    String trimmedQuery = query.trim(); 
	    if (trimmedQuery.substring(0, 6).equalsIgnoreCase("SELECT")) 
	      return Statements.SELECT; 
	    if (trimmedQuery.substring(0, 6).equalsIgnoreCase("INSERT")) 
	      return Statements.INSERT; 
	    if (trimmedQuery.substring(0, 6).equalsIgnoreCase("UPDATE")) 
	      return Statements.UPDATE; 
	    if (trimmedQuery.substring(0, 6).equalsIgnoreCase("DELETE")) 
	      return Statements.DELETE; 
	    if (trimmedQuery.substring(0, 6).equalsIgnoreCase("CREATE")) 
	      return Statements.CREATE; 
	    if (trimmedQuery.substring(0, 5).equalsIgnoreCase("ALTER")) 
	      return Statements.ALTER; 
	    if (trimmedQuery.substring(0, 4).equalsIgnoreCase("DROP")) 
	      return Statements.DROP; 
	    if (trimmedQuery.substring(0, 8).equalsIgnoreCase("TRUNCATE")) 
	      return Statements.TRUNCATE; 
	    if (trimmedQuery.substring(0, 6).equalsIgnoreCase("RENAME")) 
	      return Statements.RENAME; 
	    if (trimmedQuery.substring(0, 2).equalsIgnoreCase("DO")) 
	      return Statements.DO; 
	    if (trimmedQuery.substring(0, 7).equalsIgnoreCase("REPLACE")) 
	      return Statements.REPLACE; 
	    if (trimmedQuery.substring(0, 4).equalsIgnoreCase("LOAD")) 
	      return Statements.LOAD; 
	    if (trimmedQuery.substring(0, 7).equalsIgnoreCase("HANDLER")) 
	      return Statements.HANDLER; 
	    if (trimmedQuery.substring(0, 4).equalsIgnoreCase("CALL")) { 
	      return Statements.CALL; 
	    } 
	    return Statements.SELECT; 
    } 
    protected static enum Statements { 
		SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL,  
		CREATE, ALTER, DROP, TRUNCATE, RENAME, START, COMMIT, ROLLBACK,  
		SAVEPOINT, LOCK, UNLOCK, PREPARE, EXECUTE, DEALLOCATE, SET, SHOW,  
		DESCRIBE, EXPLAIN, HELP, USE, ANALYZE, ATTACH, BEGIN, DETACH,  
		END, INDEXED, ON, PRAGMA, REINDEX, RELEASE, VACUUM; 
    }
    
    
    
    


    public Boolean isInSystem(String name) {
	  	try {                
	  		PreparedStatement statement = c.prepareStatement("select * from `lottery_keys` where `username`='" + name + "'");
	  	    ResultSet result = statement.executeQuery();
	  	      
	  	    if (result.next()) {
	  	    	if (!result.getString("username").equalsIgnoreCase("")) {
	  	    		return true;
	  	    	}
	  	    }
	  	} catch (Exception e)  { 
	  		e.printStackTrace();
	  	}
		return false;
	}

    public Integer getKeys(String name) {
	  	try {                
	  		PreparedStatement statement = c.prepareStatement("select * from `lottery_keys` where `username`='" + name + "'");
	  	    ResultSet result = statement.executeQuery();
	  	      
	  	    if (result.next()) {
	  	        return result.getInt("keys");
	  	    } else {
	  	        return 0;
	  	    }
	  	} catch (Exception e)  { 
	  		e.printStackTrace();
	  		return 0;
	  	}
	}
    
    public void set(String name, Integer to) {
    	query("UPDATE `lottery_keys` SET `keys`='" + to + "' WHERE `username`='" + name + "'");
	}
    
    public void create(String name) {
    	query("INSERT INTO `lottery_keys`(`username`, `keys`) VALUES ('" + name + "', '0')");
	}
}
