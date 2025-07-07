const {Kafka} = require('kafkajs');


const kafka = new Kafka({
  clientId: 'transaction-generator',
  brokers: ['localhost:9092']
})

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}


const generateTransactions = async () => {
  const producer = kafka.producer()
  let transactionsPerSecond = 0;

  setInterval(() => {
    transactionsPerSecond = 0;
  }, 1000)

  await producer.connect()




  while (true) {
    for (let i = 0; i < 200; i++) {
      let senderId = Math.ceil(Math.random() * 10);
      let receiverId = Math.ceil(Math.random() * 10)
      if (senderId === receiverId) {
        receiverId = (receiverId % 10) + 1;
      }
      let amount = Math.ceil(Math.random() * 10)
      await producer.send({
        topic: 'transactions',
        messages: [
          { value: `{"senderId":${senderId},"receiverId":${receiverId},"amount":${amount}}` },
        ],
      })
      transactionsPerSecond++;
    }
    await sleep(1000);

  }
  
}

generateTransactions().then(() => {
  console.log("done");
  
})




