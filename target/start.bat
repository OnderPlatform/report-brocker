set scriptdir=%~dp0
set classpath=%scriptdir%/lib/*
java %* -cp "%classpath%" play.core.server.ProdServerStart %scriptdir%
