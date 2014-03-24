package CoreCrawler;

import java.util.List;

import json.mapping.RedditLink;
import json.mapping.RedditSubreddit;
import exception.RedditException;

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
			List<RedditSubreddit> links_sub = reddit.subreddits("");
			System.out.println("Number of subreddits exploring: " + links_sub.size());    

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

			System.out.println("Done main page. Starting sub-reddits...");

			Thread.sleep(10000); // Sleep for 10 seconds
			
			for (RedditSubreddit sub : links_sub)
			{
				System.out.println("Starting with subreddit: " + sub.getUrl());
				String subreddit = sub.getUrl().replace("/r/", "").replace("/","");

				List<RedditLink> links_for_sub =  reddit.listingFor(subreddit, "new");
								
				so_far = -1;
				while (so_far != 0)
				{
					RedditLink last_link = links_for_sub.get(links_for_sub.size()-1);

					//Get an X amount of items from item Y
					List<RedditLink> links_next = reddit.listingFor(subreddit, 100, "t3_"+last_link.getId());
					System.out.println("Links delivered: " + links_next.size());

					links_for_sub.addAll(links_next);

					so_far = links_next.size();
					System.out.println(links_for_sub.size());
				}
				
				links.addAll(links_for_sub);
				links_for_sub.clear();
				
				Thread.sleep(10000); // Sleep for 10 sec
				System.out.println("Total links so far: " + links.size());
			}
			
			
		} 
		catch( RedditException re ) 
		{
			//RedditException merely inherits Exception.
			re.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}