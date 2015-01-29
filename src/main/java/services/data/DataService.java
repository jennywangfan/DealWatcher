package services.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.RegisteredUser;
import domain.RequestSite;
import domain.SimpleItemInfo;
import domain.WatchedItem;
/**
 * 
* <p>Title: DataService</p>
* <p>Description: Class for communicating with Database</p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class DataService {

	
	private static Logger log = LoggerFactory.getLogger(DataService.class);
	/**
	 * 
	* <p>Title: saveUser</p>
	* <p>Description: Save an user</p>
	* @param user
	 */
	public void saveUser(RegisteredUser user) {
		log.debug("Saving an user");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try {
			session.save(user);
			trans.commit();
		}finally {
			if (session != null)
				session.close();
		}

	}
	
	/**
	 * 
	* <p>Title: getAllUser</p>
	* <p>Description: Get all users for caching in the system</p>
	* @return
	 */

	public List<?> getAllUser() {
		log.debug("Getting all users");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try {
			String sql = "select ru from RegisteredUser ru";
			List<?> ruList = session.createQuery(sql).list();
			trans.commit();
			return ruList;
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * 
	* <p>Title: savePriceHistory</p>
	* <p>Description: Save the lastest price info in the table for future price history function</p>
	* @param info
	 */

	public void savePriceHistory(SimpleItemInfo info) {
		log.debug("Saving price history for an url");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try {
			session.save(info);
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
	/**
	 * 
	* <p>Title: saveUserList</p>
	* <p>Description: Save a list of new users</p>
	* @param userList
	 */
	public void saveUserList(List<RegisteredUser> userList) {
		log.debug("Saving a list of user");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		int length = userList.size();
		try {
			for (int i = 1; i <= length; ++i) {
				session.merge(userList.get(i - 1));
				if (i % 100 == 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}

    /**
     * 
    * <p>Title: saveWatchedItemList</p>
    * <p>Description: Save a list of WatchedItems</p>
    * @param itemList
     */
	public void saveWatchedItemList(List<WatchedItem> itemList) {
		log.debug("Saving a list of watch items");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		int length = itemList.size();
		try {
			for (int i = 1; i <= length; ++i) {
				session.merge(itemList.get(i));
				if (i % 100 == 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * 
	* <p>Title: saveWatchedItem</p>
	* <p>Description: Save a WatchedItem </p>
	* @param item
	 */
	public void saveWatchedItem(WatchedItem item) {
		log.debug("Saving a watched item");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try {
			session.save(item);
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
	/**
	 * 
	* <p>Title: getDistinctUrls</p>
	* <p>Description: Get all distinct Urls under watching</p>
	* @return A map whose key is the supported website(see RequestSite for supported sites)
	 */

	public Map<RequestSite, List<String>> getDistinctUrls() {
		log.debug("Getting all distinct urls");
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		String sql = "select distinct siteInfo, url from WatchedItem";
		List<?> l;
		Map<RequestSite, List<String>> urlMap = new HashMap<>();
		try {
			l = session.createQuery(sql).list();
			ListIterator<?> iterator = (ListIterator<?>) l.listIterator();
			List<String> bgList = new ArrayList<>();
			List<String> bdList = new ArrayList<>();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				RequestSite siteInfo = (RequestSite) object[0];
				String url = (String) object[1];
				switch (siteInfo) {
				case BERGDORFGOODMAN:
					bgList.add(url);
					break;
				case BLOOMINGDALES:
					bdList.add(url);
					break;
				default:
					break;
				}

			}
			urlMap.put(RequestSite.BERGDORFGOODMAN, bgList);
			urlMap.put(RequestSite.BLOOMINGDALES, bdList);
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}
		return urlMap;

	}

    /**
     * 
    * <p>Title: updateWatchedItem</p>
    * <p>Description: update WatchedItem after each scheduled price digging </p>
    * @param info
     */
	public void updateWatchedItem(SimpleItemInfo info) {
		log.debug("Updating a watched item when getting an updated price");

		Session session = HibernateUtils.getSessionFactory().openSession();
		try {
			Transaction trans = session.beginTransaction();

			String sql1 = "update WatchedItem witem set lastOriginalPrice = currentOriginalPrice, lastSalePrice"
					+ " = currentSalePrice, lastStockStatus = currentStockStatus where witem.url = :url";
			String sql2 = "update WatchedItem witem set currentOriginalPrice = :originalPrice,"
					+ " currentSalePrice = :salePrice,currentStockStatus = :stockStatus, lastUpdateTimeStamp = :lastestUpdateDate where witem.url= :url";
			session.createQuery(sql1).setParameter("url", info.getUrl())
					.executeUpdate();
			session.createQuery(sql2).setParameter("url", info.getUrl())
					.setParameter("originalPrice", info.getOriginalPrice())
					.setParameter("salePrice", info.getSalePrice())
					.setParameter("stockStatus", info.getStockStatus())
					.setParameter("lastestUpdateDate",
							info.getLastestUpdateDate()).executeUpdate();
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * 
	* <p>Title: getPriceChangedItems</p>
	* <p>Description: Get a list WatchedItems whose price changed </p>
	* @return a list of WatchItems
	 */

	public List<?> getPriceChangedItems() {
		log.debug("Getting all items with price changed");
		Session session = HibernateUtils.getSessionFactory().openSession();
		List<?> itemList = null;
		try {
			Transaction trans = session.beginTransaction();
			String sql = "select witem from  WatchedItem  witem where ((witem.lastOriginalPrice <> witem.currentOriginalPrice or witem.lastSalePrice"
					+ " <> witem.currentSalePrice or witem.lastStockStatus <> witem.currentStockStatus) and (witem.initialOriginalPrice <> witem.currentOriginalPrice or "
					+ "witem.initialSalePrice <> witem.currentSalePrice or witem.initialStockStatus <> witem.currentStockStatus)) group by witem.url";
			itemList = session.createQuery(sql).list();

			trans.commit();

		} finally {
			if (session != null)
				session.close();
		}
		return itemList;
	}

	/**
	 * 
	* <p>Title: removeUrlList</p>
	* <p>Description: Remove a list of Urls</p>
	* @param ru
	* @param urlList
	 */

	public void removeUrlList(RegisteredUser ru, List<String> urlList) {
		
		log.debug("Removing a list of urls");
		Session session = HibernateUtils.getSessionFactory().openSession();
		String sql1 = "delete from WatchedItem wi where wi.user = :ru and wi.url in :uList1";
		
		try {
			Transaction trans = session.beginTransaction();
			session.createQuery(sql1).setParameter("ru", ru).setParameterList("uList1", urlList).executeUpdate();
			trans.commit();
		} finally {
			if (session != null)
				session.close();
		}

	}

}
