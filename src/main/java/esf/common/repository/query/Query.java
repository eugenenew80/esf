package esf.common.repository.query;

import java.util.Map;

public interface Query {
	String where();
	String orderBy();
	Map<String, MyQueryParam> params();
}
