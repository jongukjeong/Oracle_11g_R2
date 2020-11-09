package bookshop;

import java.util.List;

public interface AuthorDAO {
	public List<AuthorVO> getList();
	public AuthorVO get(Long id);
	public boolean insert(AuthorVO authorVO);
	public boolean delete(Long id);
	public boolean update(AuthorVO authorVO);
}
