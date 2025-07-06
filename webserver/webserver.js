const { log } = require('console');
const express = require('express')
const {Kafka} = require('kafkajs');
const app = express();
const server = require('http').createServer(app);
const WebSocket = require('ws')

const wss = new WebSocket.Server({server : server, path : '/stats'})

app.use(express.static("../webui/dist"))

const kafka = new Kafka({
  clientId: 'transaction-webserver',
  brokers: ['localhost:9092']
})

const statsConsumer = kafka.consumer({groupId : 'transaction-stats'})
const userConsumer = kafka.consumer({groupId: 'users'});

let stats;
let users = {};

const initStatsConsumer = async() => {
  await statsConsumer.connect()
  await statsConsumer.subscribe({topics : ['transaction-stats']});
  await statsConsumer.run({  
    eachMessage: async ({ topic, partition, message, heartbeat, pause }) => {
      stats = message.value.toString()
    },
  })
}

const initUserConsumer = async() => {
  await userConsumer.connect()
  await userConsumer.subscribe({topics : ['users']});
  await userConsumer.run({  
    eachMessage: async ({ topic, partition, message, heartbeat, pause }) => {
      const parsedMessage = JSON.parse(message.value.toString())
      users[parsedMessage.name] = parsedMessage.balance;
    },
  })
}

wss.on('connection', (ws) => {
  let statsInterval;
  let userInterval;
  console.log('websocket established')
  setInterval(() => {
    ws.send('ping')
  }, 5000)

  ws.on('message', (msg) => {
    msg = msg.toString();
    if (msg === 'start') {
      statsInterval = setInterval(() => {
        if (stats) {
          ws.send(stats)
        }
        
      }, 1000)

      userInterval = setInterval(() => {
        ws.send(JSON.stringify(users))
      }, 1000)
    }
    else if (msg === 'stop') {
      clearInterval(statsInterval);
      clearInterval(userInterval);
    }
  })
})


const main = async() => {
  await initStatsConsumer();
  await initUserConsumer();
  server.listen(9090);
  console.log("server listening on port 9090...")
}


main();



