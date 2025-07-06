for i in $(ps -ef | grep transaction-generator.js | grep -v 'grep' | cut -d " " -f 4);
do
  kill $i;
done