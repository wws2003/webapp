package com.techburg.autospring.db.task.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.techburg.autospring.bo.abstr.IBuildInfoBo;
import com.techburg.autospring.db.task.abstr.AbstractDBTask;
import com.techburg.autospring.model.business.BuildInfo;
import com.techburg.autospring.model.entity.BuildInfoEntity;
import com.techburg.autospring.model.query.BuildInfoPersistenceQuery;
import com.techburg.autospring.model.query.BuildInfoPersistenceQuery.BuildInfoDataRange;
import com.techburg.autospring.service.abstr.PersistenceResult;

public class BuildInfoDBTaskImpl extends AbstractDBTask {

	public static final int TASK_TYPE_PERSIST = 0;
	public static final int TASK_TYPE_GET_NUMBER = 1;
	public static final int TASK_TYPE_LOAD = 2;
	public static final int TASK_TYPE_REMOVE_BY_ID = 3;

	private int mTaskType = 0;
	private IBuildInfoBo mBuildInfoBo = null;
	private EntityManagerFactory mEntityManagerFactory = null;

	// parameters set from outside
	private BuildInfo mParamBuildInfoToPersist = null;
	private BuildInfoPersistenceQuery mParamBuildInfoLoadQuery = null;
	private long mParamIdForRemove = -1;
	private long mParamWorkspaceId = -1;

	// Results calculated by execute()
	private int mResultForPersist = -1;
	private int mResultForLoad = -1;
	private int mResultForRemove = -1;
	private List<BuildInfo> mResultBuildInfoListLoad = new ArrayList<BuildInfo>();
	private long mResultNumber = -1;

	public BuildInfoDBTaskImpl(IBuildInfoBo buildInfoBo, EntityManagerFactory etmFactory) {
		mBuildInfoBo = buildInfoBo;
		mEntityManagerFactory = etmFactory;
	}

	public void setPersistParam(BuildInfo buildInfoToPersist) {
		mParamBuildInfoToPersist = buildInfoToPersist;
		mTaskType = TASK_TYPE_PERSIST;
		setDBReadWriteMode(DB_WRITE_MODE);
	}

	public int getPersistResult() {
		return mResultForPersist;
	}

	public void setGetNumberParam(long workspaceId) {
		mParamWorkspaceId = workspaceId;
		mTaskType = TASK_TYPE_GET_NUMBER;
		setDBReadWriteMode(DB_READ_MODE);
	}

	public long getBuildInfoNumberResult() {
		return mResultNumber;
	}

	public void setLoadParam(BuildInfoPersistenceQuery paramBuildInfoLoadQuery) {
		mParamBuildInfoLoadQuery = paramBuildInfoLoadQuery;
		mTaskType = TASK_TYPE_LOAD;
		setDBReadWriteMode(DB_READ_MODE);
	}

	public int getLoadResult(List<BuildInfo> resultBuildInfoListLoad) {
		resultBuildInfoListLoad.clear();
		resultBuildInfoListLoad.addAll(mResultBuildInfoListLoad);
		mResultBuildInfoListLoad.clear();
		return mResultForLoad;
	}

	public void setRemoveParam(long removeId) {
		mParamIdForRemove = removeId;
		mTaskType = TASK_TYPE_REMOVE_BY_ID;
		setDBReadWriteMode(DB_WRITE_MODE);
	}

	public int getRemoveResult() {
		return mResultForRemove;
	}

	@Override
	public void execute() {
		switch (mTaskType) {
		case TASK_TYPE_PERSIST:
			mResultForPersist = persistBuildInfo(mParamBuildInfoToPersist);
			break;
		case TASK_TYPE_GET_NUMBER:
			mResultNumber = getNumberOfPersistedBuildInfoOfWorkspace(mParamWorkspaceId);
			break;
		case TASK_TYPE_LOAD:
			mResultBuildInfoListLoad.clear();
			mResultForLoad = loadPersistedBuildInfo(mResultBuildInfoListLoad, mParamBuildInfoLoadQuery);
			break;
		case TASK_TYPE_REMOVE_BY_ID:
			mResultForRemove = removeBuildInfoByID(mParamIdForRemove);
			break;
		default:
			break;
		}
	}

	private int persistBuildInfo(BuildInfo buildInfo) {
		BuildInfoEntity entity = mBuildInfoBo.getEntityFromBusinessObject(buildInfo);
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		try {
			entityManager.persist(entity);
			entityManager.detach(entity); // Do not need to manage this object
											// longer !
			tx.commit();
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			tx.rollback();
			return PersistenceResult.PERSISTENCE_FAILED;
		} finally {
			entityManager.close();
		}
		return PersistenceResult.PERSISTENCE_SUCCESSFUL;
	}

	private int loadPersistedBuildInfo(List<BuildInfo> buildInfoList, BuildInfoPersistenceQuery query) {
		Query loadQuery = null;
		buildInfoList.clear();
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		StringBuilder queryStringBuilder = new StringBuilder();

		switch (query.getDataRange()) {
		case BuildInfoDataRange.ALL:
			queryStringBuilder.setLength(0);
			queryStringBuilder.append("select * from build_info");

			if (query.getWorkspaceId() > 0) {
				queryStringBuilder.append(" where workspace_id = ").append(query.getWorkspaceId());
			}

			loadQuery = entityManager.createNativeQuery(queryStringBuilder.toString(), BuildInfoEntity.class);
			try {
				@SuppressWarnings("unchecked")
				List<BuildInfoEntity> entities = loadQuery.getResultList();
				for (BuildInfoEntity entity : entities) {
					entityManager.detach(entity);
					buildInfoList.add(mBuildInfoBo.getBusinessObjectFromEntity(entity));
				}
				return PersistenceResult.LOAD_SUCCESSFUL;
			} catch (Exception e) {
				e.printStackTrace();
				return PersistenceResult.INVALID_QUERY;
			}
		case BuildInfoDataRange.PAGE_MATCH:
			int page = query.getPage();
			int nbPage = query.getNbInstancePerPage();

			queryStringBuilder.append("select * from ").append("(").append("select * from build_info ")
					.append("where workspace_id = ").append(query.getWorkspaceId()).append(" order by id desc ").append("limit ")
					.append(page * nbPage).append(") ");

			if (page > 1) {
				queryStringBuilder.append("where id < ").append("(").append("select min(id) from (select id from build_info")
						.append(" where workspace_id = ").append(query.getWorkspaceId()).append(" order by id desc limit ")
						.append((page - 1) * nbPage).append("))");
			}

			loadQuery = entityManager.createNativeQuery(queryStringBuilder.toString(), BuildInfoEntity.class);
			try {
				@SuppressWarnings("unchecked")
				List<BuildInfoEntity> entities = loadQuery.getResultList();
				for (BuildInfoEntity entity : entities) {
					entityManager.detach(entity);
					buildInfoList.add(mBuildInfoBo.getBusinessObjectFromEntity(entity));
				}
				return PersistenceResult.LOAD_SUCCESSFUL;
			} catch (Exception e) {
				e.printStackTrace();
				return PersistenceResult.INVALID_QUERY;
			}
		case BuildInfoDataRange.LIMITED_MATCH:
			long firstId = query.getFirstId();
			long lastId = query.getLastId();

			queryStringBuilder.append("select * from build_info").append(" ").append("where id between").append(" ")
					.append(firstId).append(" ").append("and").append(" ").append(lastId);
			if (query.getWorkspaceId() > 0) {
				queryStringBuilder.append(" and workspace_id = ").append(query.getWorkspaceId());
			}

			loadQuery = entityManager.createNativeQuery(queryStringBuilder.toString(), BuildInfoEntity.class);
			try {
				@SuppressWarnings("unchecked")
				List<BuildInfoEntity> entities = loadQuery.getResultList();
				for (BuildInfoEntity entity : entities) {
					entityManager.detach(entity);
					buildInfoList.add(mBuildInfoBo.getBusinessObjectFromEntity(entity));
				}
				return PersistenceResult.LOAD_SUCCESSFUL;
			} catch (Exception e) {
				e.printStackTrace();
				return PersistenceResult.INVALID_QUERY;
			}

		case BuildInfoDataRange.ID_MATCH:
			long id = query.getId();
			BuildInfoEntity entity = entityManager.find(BuildInfoEntity.class, id);
			if (entity != null) {
				entityManager.detach(entity);
				buildInfoList.add(mBuildInfoBo.getBusinessObjectFromEntity(entity));
			}
			return PersistenceResult.LOAD_SUCCESSFUL;
		default:
			return PersistenceResult.INVALID_QUERY;
		}
	}

	private int removeBuildInfoByID(long id) {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		int ret = PersistenceResult.REMOVE_FAILED;
		tx.begin();
		try {
			BuildInfoEntity entityToDelete = entityManager.find(BuildInfoEntity.class, id);
			if (entityToDelete != null) {
				entityManager.remove(entityToDelete);
				ret = PersistenceResult.REMOVE_SUCCESSFUL;
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			ret = PersistenceResult.REMOVE_FAILED;
		} finally {
			entityManager.close();
		}
		return ret;
	}

	private long getNumberOfPersistedBuildInfoOfWorkspace(long workspaceId) {
		EntityManager entityManager = mEntityManagerFactory.createEntityManager();
		Query loadQuery = entityManager.createNamedQuery("findNumberOfRecordsOfWorkspace");
		loadQuery.setParameter("workspace", workspaceId);
		long numberOfPersistedBuildInfo = (Long) loadQuery.getSingleResult();
		entityManager.close();
		return numberOfPersistedBuildInfo;
	}
}
