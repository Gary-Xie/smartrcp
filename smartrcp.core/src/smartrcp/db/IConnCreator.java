package smartrcp.db;

import java.sql.Connection;
/**
 * ���ݿ�����Creator
 * @author pengzhen
 *
 */
public interface IConnCreator {
   Connection getConnection(String url,String name,String pwd);
}
