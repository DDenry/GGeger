package com.GGeger.pattern.chain;

public abstract class EntityInterpreter {

	protected String className;
	//
	protected EntityInterpreter nextInterpreter;

	public void setNextInterpreter(EntityInterpreter nextInterpreter) {
		this.nextInterpreter = nextInterpreter;
	}

	public <T> void distribute(T entity) {

		// 匹配解释器与实体
		if (this.className.equals(entity.getClass().getSimpleName())) {

			interpreted(entity);

		} else {

			if (nextInterpreter != null)

				nextInterpreter.distribute(entity);

			else {

				System.err.println(entity.getClass().getSimpleName() + " doesn't have his own interpreter!");

			}
		}
	}

	abstract protected <T> void interpreted(T entity);
}
