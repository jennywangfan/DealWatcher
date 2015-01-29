
  What is it?
  -----------

  This project is a deal watcher. user could use email to sign up then 
  send email with the links of interested items and notifications will 
  be sent if the price or stock status changed. The current version 
  supports email from gmail and hotmail and accept links from 
  BergdorfGoodman and BloomingDales.

  How to run it?
  ------------

  This is a maven project, make sure you have internet connection for
  downloading all dependencies. other than that, you need JDK 1.8(
  since the default method is used in interface). Database is MySQL.

  First :  Set up your Database.
  After you make your Database running, config 
  DealWatcher/src/main/java/hibernate.cfg.xml . If you are using MySQL,
  set up 3 properties to your own values : connection.url , connection.
  username, connection.password. If you are using other Database, set 
  up 2 more properties : connection.driver_class and dialect.

  Second : Set up your email server(I am using gmail for receiving and 
  sending emails)
  Config bean.xml
  setup username and password in that file if you are using Gmail, else
  set up all mail properties of your own email server.

  Third : You are good to go
  Run BootMain and then you can email to get signed up and get your items
  watched.

  Email Sample: subject are non case-sensitive
  For Signup :  Email with subject "signup"
  For Watch your items:  Email with subject "watch" and links in contents. 
  please add lines between different links.
  For remove your items: Email with subject "remove" and links in contents.
  

  Other information for developers
  ------------
  Check the docs folder to see the sequence diagram for the whole process.
  If using in high volumn and multi-processor system, You could set a large 
  thread pool for sending emails in bean.xml. Process for checking different
  sites are running in multi-threading mode, if the time setting for two 
  requests is not longer enough, the request might be denied by the website, 
  try to think about using proxy or make the thread sleep longer.

  Licensing
  ---------

  Please see the file called LICENSE.