package CoreCrawler;

import java.util.List;

import json.mapping.RedditLink;
import wrapper.PredIOConn;
import exception.RedditException;

/**
 * @author: Eric Eidelberg
 * @email: ninuson123@gmail.com
 * 
 * This is part of the demo application for the Prediction-IO recommendation engine.
 * This program establishes connection and gets the first 1,000 items on the front page of reddit. 
 * It then sends those to the recommendation engine for later retrieval by the client application.
 * 
 */

public class CrawlerDriver 
{	
	public static void main(String[] args)
	{
		Reddit reddit = new Reddit("testing_122");
		try 
		{
			// Login with a user
			//    	    RedditJsonMessage response = reddit.login( "testing_122", "1234" );
			//    	    System.out.println(response.toString());

			// Get user information
			//    	    RedditAccount account = reddit.meJson();
			//    	    System.out.println(account.toString());

			// Get top 25 subreddits.
			//			List<RedditSubreddit> links_sub = reddit.subreddits("");
			//			System.out.println("Number of subreddits exploring: " + links_sub.size());    

			System.out.println("Starting with main menu: ");
			List<RedditLink> links = reddit.listingForMainPage();
			System.out.println("Links delivered: " + links.size());
			
			int so_far = -1;
			while (so_far != 0)
			{
				RedditLink last_link = links.get(links.size()-1);

				//Get an X amount of items from item Y
				List<RedditLink> links_next = reddit.listingForMainPage(100, "t3_"+last_link.getId());
				System.out.println("Links delivered: " + links_next.size());

				links.addAll(links_next);

				so_far = links_next.size();
				System.out.println(links.size());
			}
			
			// This part can feed another 21k~ links or so. Reddit allows only 30 requests per minute, hence the sleep timers.

			System.out.println("Done main page. Added " + links.size() + " to the list.");
//
//			Thread.sleep(10000); // Sleep for 10 seconds
//			
//			for (RedditSubreddit sub : links_sub)
//			{
//				System.out.println("Starting with subreddit: " + sub.getUrl());
//				String subreddit = sub.getUrl().replace("/r/", "").replace("/","");
//
//				List<RedditLink> links_for_sub =  reddit.listingFor(subreddit, "new");
//								
//				so_far = -1;
//				while (so_far != 0)
//				{
//					RedditLink last_link = links_for_sub.get(links_for_sub.size()-1);
//
//					//Get an X amount of items from item Y
//					List<RedditLink> links_next = reddit.listingFor(subreddit, 100, "t3_"+last_link.getId());
//					System.out.println("Links delivered: " + links_next.size());
//
//					links_for_sub.addAll(links_next);
//
//					so_far = links_next.size();
//					System.out.println(links_for_sub.size());
//				}
//				
//				links.addAll(links_for_sub);
//				links_for_sub.clear();
//				
//				Thread.sleep(10000); // Sleep for 10 sec
//				System.out.println("Total links so far: " + links.size());
//			}
			
			// ========================== BEGINING OF ENGINE CONNECTION ===========================================
			
			System.out.println("Creating connection to Recomendation Engine...");
			/* set appurl to your API server */
		        String appurl = "http://localhost:8000";
		        /* set appkey for the engine. */
		        String appkey_2 = "ZL70ye3wxYGlfJGVX077r8R4XsB6oM60PXoqDfxbgDk3pOppnxKCj02ztOAVLrET";
				
		        // Initiate the connector
			PredIOConn conn = new PredIOConn(appurl, appkey_2);
			// Set debug console printouts on.
			conn.setDebug(true);
			
			System.out.println("Connected!");
			System.out.println("Begin sending info to recommendation engine.");
			
			String[] itypes = {"main_page"};
			
			for (RedditLink link : links)
			{
				// t3_ is a static prefix for links on the reddit api
				String link_id = "t3_"+link.getId();
				conn.addItem(link_id, itypes);
			}
			
			System.out.println("Done!");
		} 
		catch( RedditException re ) 
		{
			//RedditException merely inherits Exception.
			re.printStackTrace();
		} 		
	}
}
