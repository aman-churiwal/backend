hostname=http://localhost:8080
quiz_external_reference=$(http -j POST $hostname/viteezy/api/quiz/ | jq .externalReference | sed 's/"//g')
first_name=viteezy-user-$(date +%s)
last_name=surname-$(date +%s)
email=viteezy-userrr+$(date +%s)@unicornifylabs.com

http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/allergies/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/amount-of-training/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/body-type-currents/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/body-type-targets/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/energy-level/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/find-us/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/fruit-consumptions/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/genders/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/hours-of-sleep/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/minutes-of-sun/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/nutrition-consumptions/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/pregnancies/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/smokes/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/sport-types/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/stress-level/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/usage-reason/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/used-supplements/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/vegetable-consumptions/1
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/height heightCm=180
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/weight weightGr=75000
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/weight-desired weightDesiredGr=80000
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/date-of-birth date=1990-09-15
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/name name=${first_name}
http -v -j POST $hostname/viteezy/api/quiz/$quiz_external_reference/answer/email email=${email}

http -v -j GET $hostname/viteezy/api/quiz/$quiz_external_reference/answers
blend_external_reference=$(http -j POST $hostname/viteezy/api/quiz/${quiz_external_reference}/blends | jq .blendExternalReference | sed 's/"//g')
sleep 1
http -v -j GET $hostname/viteezy/api/blends/$blend_external_reference
http -v -j GET $hostname/viteezy/api/blends/$blend_external_reference/ingredients

# Add flavour
flavour_id=$(http -j GET $hostname/viteezy/api/ingredients | jq -c '.[] | select( .isAFlavour) | .id' | head -n 1)
http -v -j POST $hostname/viteezy/api/blends/$blend_external_reference/ingredients/${flavour_id} amount=50 isUnit=ML

# Customer
http -v -j PUT $hostname/viteezy/api/customer/blend/$blend_external_reference firstName=${first_name} lastName=${last_name} phoneNumber=phoneNumber email=$email address=address postcode=postcode city=city

# Payment
http -v -j POST $hostname/viteezy/api/payment/blend/$blend_external_reference
# In case you want to use a coupon/discount
#http -v -j POST $hostname/viteezy/api/payment/blend/$blend_external_reference couponCode=henk

sleep 1
#http -v --form POST $hostname/viteezy/api/payment/callback id=$blend_external_reference
