# test-automation
```shell
mvn clean install -DsuiteXmlFile=testng_all.xml -Dtestname=all -Ddataproviderthreadcount=1 -Dtest.auto.driver.chrome.headless=true -Dcucumber.filter.tags=@US1
```