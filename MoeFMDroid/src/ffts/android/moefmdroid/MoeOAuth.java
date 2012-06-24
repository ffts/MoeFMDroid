package ffts.android.moefmdroid;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class MoeOAuth {

	private CommonsHttpOAuthConsumer consumer;
	private CommonsHttpOAuthProvider provider;
	private String access_token,access_token_secret,verifier;
	public static String CONSUMERKEY = "2bbeff3c3349208e0e4f8bad2b1b2f4a04fd6f3fd";
	public static String CONSUMERSECRET = "79ec971c5dd50e848cd58b5c7399c6f0";
	
	MoeOAuth(	){
		consumer = new CommonsHttpOAuthConsumer(CONSUMERKEY, CONSUMERSECRET);
		provider = new CommonsHttpOAuthProvider("http://api.moefou.org/oauth/request_token", 
																			"http://api.moefou.org/oauth/access_token", 
																			"http://api.moefou.org/oauth/authorize");
	}
	
	public String getAccess_Token(){
		return this.access_token;
	}
	
	public String getAccess_Token_Secret(){
		return this.access_token_secret;
	}
	
	public String getVerifier(){
		return this.verifier;
	}
	
	public void setVerifier(String verifier){
		this.verifier = verifier;
	}
	
	public String getRequestToken() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
		
		String callBackUrl="mfmd://OAuthActivity";
		String oauthURL = provider.retrieveRequestToken(consumer, callBackUrl);
		return oauthURL;
		
	}
	
	public void getAccessToken() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
		
		provider.setOAuth10a(true);
		provider.retrieveAccessToken(consumer, verifier);
		this.access_token = consumer.getToken();
		this.access_token_secret = consumer.getTokenSecret();
	}
	
}
