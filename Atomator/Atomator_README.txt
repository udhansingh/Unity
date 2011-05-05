Atomator
========
The purpose of Atomator is to enable clients to collect information from
various sources news, email, social networks (RSS, Atom, custom formats).

The primary objective of this project is facilitate collection of information;
upconvert various different formats into a widely known ATOM format.  This lets
the application to do anything with the atom feeds.  Essentially, ATOM is the 
open data model here.

Atomator can be scheduled to collect feeds; the interface will then enable clients
to consume new feeds arriving.  Collection can be scheduled to be purged.

Out of band OAuth is used in this project.  This is a good mechanism to ensure that 
the tools using this facility are humans and not robots.

Currently, command line authentication verifier is used, however this can be easily 
adapted to do app specific needs via spring injections.  

Dependencies
============
1.	abdera-activitystreams : http://code.google.com/p/abdera-activitystreams/
	Used to generate Activity Streams feed.
	
2.	Scribe OAuth Java Library : http://github.com/fernandezpablo85/scribe-java
	I've bundled the scribe code whose functionality is sufficient for Atomator
	
	The reason for this is to add mandatory "oauth" field "scope" for supporting
	Google accounts.
	
	"scope" outh parameter for Google accounts enables access to all services specified
	in the scope in one request.


Thirdparty Libraries
====================
	http://abdera.apache.org/
			abdera-1.1.jar
	
	http://commons.apache.org/codec/
			commons-codec-1.4.jar
	
	http://commons.apache.org/dbcp/
			commons-dbcp-1.4.jar
	
	commons-lang-2.4.jar
			http://commons.apache.org/lang/
	
	http://commons.apache.org/logging/
			commons-logging-1.1.1.jar
	
	http://commons.apache.org/pool/
			commons-pool-1.5.4.jar
	
	http://hsqldb.org/
			hsqldb.jar
	
	http://www.quartz-scheduler.org/
			quartz-1.8.3.jar
	
	http://incubator.apache.org/wink/downloads.html
			wink-1.1.1-incubating.jar
	
	https://jersey.dev.java.net/
			jsr311-api-1.1.1.jar									
	
	http://logging.apache.org/log4j/1.2/download.html
			log4j-1.2.15.jar
	
	http://www.slf4j.org/download.html
			slf4j-api-1.5.10.jar
			slf4j-log4j12-1.5.10.jar
	
	http://www.springsource.org/download
			org.springframework.asm-3.0.3.RELEASE.jar
			org.springframework.beans-3.0.3.RELEASE.jar
			org.springframework.context.support-3.0.3.RELEASE.jar
			org.springframework.context-3.0.3.RELEASE.jar
			org.springframework.core-3.0.3.RELEASE.jar
			org.springframework.expression-3.0.3.RELEASE.jar
			org.springframework.jdbc-3.0.3.RELEASE.jar
			org.springframework.transaction-3.0.3.RELEASE.jar

			
Application registration with channels
=======================================
Twitter				http://dev.twitter.com/apps
					http://dev.twitter.com/doc
					
Streamwork			https://streamwork.com/oauth_clients
					https://streamwork.com/api/Authentication.html
					
LinkedIn			https://www.linkedin.com/secure/developer
					http://developer.linkedin.com/community/apis
					
Google				https://www.google.com/accounts/ManageDomains
					http://code.google.com/apis/accounts/

TripIt				http://www.tripit.com/developer/create
					http://groups.google.com/group/api_tripit/web/tripit-api-documentation---v1
					
FaceBook			http://www.facebook.com/developers/apps.php
					http://developers.facebook.com/docs/reference/api/
					
Yahoo				https://developer.apps.yahoo.com/projects
					http://developer.yahoo.com/oauth/


Text classification Services
============================
uClassify			http://www.uclassify.com
					Read Key: 4YYLXtnmPyE4TfOSJQFz14CBi0w
					