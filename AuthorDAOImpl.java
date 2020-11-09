package bookshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAOImpl implements AuthorDAO {
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(dburl, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC 드라이버 로드 실패!");
		}
		return conn;
	}
	
	@Override
	public List<AuthorVO> getList() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		//	데이터 전송을 위한 리스트
		List<AuthorVO> list = new ArrayList<>();
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT author_id, author_name, " +
				"author_desc FROM author";
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				AuthorVO authorVO = new AuthorVO(
						rs.getLong(1),
						rs.getString(2),
						rs.getString(3)
						);
				list.add(authorVO);
			}
		} catch (SQLException e) {
			System.err.println("ERROR:" + e.getMessage());
		}
		
		return list;
	}

	@Override
	public AuthorVO get(Long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		AuthorVO authorVO = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = "SELECT author_id, " +
					"author_name, author_desc " +
					"FROM author " +
					"WHERE author_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id); // 바인딩
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				authorVO = new AuthorVO(
						rs.getLong(1),
						rs.getString(2),
						rs.getString(3)
						);
			}
		} catch (SQLException e) {
			System.err.println("ERROR:" + e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				System.err.println("ERROR:" + e.getMessage());
			}
		}
		return authorVO;
	}

	@Override
	public boolean insert(AuthorVO authorVO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int insertedCount = 0;
		
		try {
			conn = getConnection();
			String sql = "INSERT INTO author " +
					"VALUES(SEQ_AUTHOR_ID.NEXTVAL, " +
					"?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, authorVO.getAuthorName());
			pstmt.setString(2, authorVO.getAuthorDesc());
			
			insertedCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("ERROR:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				System.err.println("ERROR:" + e.getMessage());
			}
		}
		return insertedCount == 1;
	}

	@Override
	public boolean delete(Long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int deletedCount = 0;
		
		try {
			conn = getConnection();
			String sql = "DELETE FROM author WHERE author_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			
			deletedCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("ERROR:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				System.err.println("ERROR:" + e.getMessage());
			}
		}
		return deletedCount == 1;
	}

	@Override
	public boolean update(AuthorVO authorVO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updatedCount = 0;
		
		try {
			conn = getConnection();
			String sql = "UPDATE AUTHOR SET " +
					"author_name=?, author_desc=? " +
					"WHERE author_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, authorVO.getAuthorName());
			pstmt.setString(2, authorVO.getAuthorDesc());
			pstmt.setLong(3, authorVO.getAuthorId());
			
			updatedCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("ERROR:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				System.err.println("ERROR:" + e.getMessage());
			}
		}
		return 1 == updatedCount;
	}

}
