package esf.common.repository.query;

import esf.common.repository.query.QueryImpl.QueryBuliderImpl;

public interface QueryBuilder {
	 QueryBuilder setParameter(String field, Object value);
	 QueryBuilder setParameter(String field, MyQueryParam param);
	 QueryBuliderImpl setOrderBy(String orderBy);
	 Query build();
}
