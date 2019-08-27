#!/usr/bin/env bash

set -x

URL=pal-tracker-nw.apps.evans.pal.pivotal.io/time-entries

http POST $URL projectId=1 userId=2 date="2019-01-01" hours=8

TIME_ENTRY_ID=$(http --body $URL | jq .[-1].id)

http $URL/$TIME_ENTRY_ID

http PUT $URL/$TIME_ENTRY_ID projectId=88 userId=99 date="2019-01-01" hours=8

http DELETE $URL/$TIME_ENTRY_ID


