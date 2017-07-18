(개발 환경은 tomcat + intellij로 설명한다.)

[Spring framework 셋업 하기]

1.git에서 다운로드 받는다. (https://github.com/yds5004/martn_example_api)

2. project setting에서 project SDK를 확인한다.

3. module > sources 탭에서 아래와 같이 "src"로 되어 있는 지 확인한다. (만약 "src"로 되어 있지 않으면, "+Add Content Root”영역에서 X로 제거한 후 다시 "+Add Content Root”를 눌러서 “serverapi”를 선택한다.)

4. module > paths 탭에서 “compiler output”의 경로를 지정한다. (project파일의 web/WEB-INF/classes이어야 한다.)

5. module > Dependencies 탭에서 화면 오른쪽의 “+” 버튼을 눌러서 “jar or directories…”를 선택한 후, web/WEB-INF/lib/*.jar 모든 jar파일을 선택한다.

6. 프로젝트를 빌드하여 classes파일을 생성한다. (빌드 방법: Build > Build Project)



[Tomcat 셋업 하기]

ubuntu에서 apt-get install tomcat 설치 시,
  - bin dir: /usr/share/tomcat8/bin
  - conf dir: /etc/tomcat8/server.xm
  - log file: /var/log/tomcat8/catalina.outl
  - restart: service tomcat8 restart

server.xml에서 아래와 같이 세 부분을 수정한다.
(1) 한글 처리를 위해서 UTF-8 추가
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               URIEncoding="UTF-8"
               redirectPort="8443" />
(2) 한글 처리를 위해서 UTF-8 추가
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" URIEncoding="UTF-8" />

(3) tomcat home 경로 설정
<Host name="localhost"  appBase="/home/dsyoon/workspace/examples/web" unpackWARs="true" autoDeploy="true">
        <Context path="" docBase="/home/dsyoon/workspace/examples/web" reloadable="true"/>
        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />

위와 같이 설정 후, tomcat을 실행한다.
$ service tomcat8 restart



[WEB 테스트 하기]
브라우져에서 아래의 테스트 url을 입력하여 확면을 확인한다.
test url:
    http://localhost:8080/home.ncue?cmd=info&query=test_input
	http://localhost:8080/home.ncue?cmd=hello&query=test_input
	http://localhost:8080/home.ncue?cmd=getData&query=test_input