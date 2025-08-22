#!/bin/bash
#this script will test category GET/POST and answer GET/POST endpoints
#you need jq: https://stedolan.github.io/jq/download/

hostname=http://localhost:8080
category_url=$(curl -s -w GET "$hostname/viteezy/api/swagger.json" | grep "/category" | grep -o '".*"' | sed 's/"//g' | grep '[^}]$')

#basic category GET and POST
for item in $category_url;
do
  http_response_get_category=$(curl -o /dev/null -s -w "%{http_code}\n" "$hostname/viteezy/api$item" -H "accept: application/json")
  http_response_post_category=$(curl -o /dev/null -s -w "%{http_code}\n" "$hostname/viteezy/api$item" -H "accept: application/json" -H "Content-Type: application/json" -d '{"name": "test","code": "'.$RANDOM.'"}')
  http_response_post_category_subtitle=$(curl -o /dev/null -s -w "%{http_code}\n" "$hostname/viteezy/api$item" -H "accept: application/json" -H "Content-Type: application/json" -d '{"name": "test","code": "'$RANDOM'", "subtitle": "subtitle"}')
  if [ $http_response_get_category == 200 ]; then
	echo OK $http_response_get_category GET category $item
  else
    echo ERROR $http_response_get_category GET category $item
	exit -1
  fi
  if [ $http_response_post_category == 201 ]; then
	echo OK $http_response_post_category POST category $item
  elif [ $http_response_post_category_subtitle == 201 ]; then
	echo OK $http_response_post_category_subtitle POST category $item
  else
    echo ERROR $http_response_post_category POST category $item
	exit -1
  fi
done

#basic answer GET and POST
external_reference=$(curl -s -X POST "$hostname/viteezy/api/quiz" | jq -r '.externalReference')

answer_url=$(curl -s -w GET "$hostname/viteezy/api/swagger.json" | grep "/answer" | grep -o '".*"' | sed 's/"//g' | grep '[^}]$' | sed 's,/quiz/{quizExternalReference},,g')

for item in $answer_url; do
	#this will skip all the open values categories
	if [[ $item == *"email"* ]] || [[ $item == *"height"* ]] || [[ $item == *"weight"* ]] || [[ $item == *"weight-desired"* ]] || [[ $item == *"name"* ]] || [[ $item == *"date-of-birth"* ]] || [[ $item == *"answers"* ]]; then
		continue
	else
		http_response_post_answer=$(curl -X POST -o /dev/null -s -w "%{http_code}\n" "$hostname/viteezy/api/quiz/$external_reference$item/1" -H "accept: application/json")
		http_response_get_answer=$(curl -o /dev/null -s -w "%{http_code}\n" "$hostname/viteezy/api/quiz/$external_reference$item" -H "accept: application/json")
		if [ $http_response_get_answer == 200 ]; then
			echo OK $http_response_get_answer GET answer $item
		else
			echo ERROR $http_response_get_answer GET answer $item
			exit -1
		fi
		if [ $http_response_post_answer == 201 ]; then
			echo OK $http_response_post_answer POST answer $item
		else
			echo ERROR $http_response_post_answer POST answer $item
			exit -1
		fi
	fi
done
