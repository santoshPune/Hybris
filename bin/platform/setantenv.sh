#!/bin/bash
OWN_NAME=setantenv.sh

if ([[ -n "$ZSH_EVAL_CONTEXT" && "$ZSH_EVAL_CONTEXT" == "toplevel" ]] ||
	[[ -n "$BASH_VERSION" && "$0" == "$BASH_SOURCE" ]]); then
	echo "* Please call as '. ./$OWN_NAME', not './$OWN_NAME' !!!---"
	echo "* Also please DO NOT set back the executable attribute"
	echo "* On this file. It was cleared on purpose."
	
	chmod -x ./$OWN_NAME
	exit
fi

export PLATFORM_HOME=`pwd`
export ANT_OPTS="-Xmx512m -Dfile.encoding=UTF-8"
export ANT_HOME="$PLATFORM_HOME/apache-ant-1.9.1"
chmod +x "$ANT_HOME/bin/ant"
chmod +x "$PLATFORM_HOME/license.sh"
if [[ ! "$PATH" =~ "$ANT_HOME" ]]; then
    export PATH=$ANT_HOME/bin:$PATH
fi
