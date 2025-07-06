import { useState } from 'react'
import {Button, Flex} from 'antd'
import './App.css'
const webSocketUrl = 'wss://junshernlim.dev/payments-simulator/stats'
import useWebSocket, { ReadyState } from 'react-use-websocket';
import { useEffect } from 'react';
import TransactionGrid from './TransactionsGrid';
import UsersGrid from './UsersGird';


const START = 0;
const STOP = 1;

function App() {
  const [simulatorState, setSimulatorState] = useState(STOP);
  const [transactions, setTransactions] = useState(0)
  const [mostRecentTransactions, setMostRecentTransactions] = useState([]);
  const [users, setUsers] = useState({});

  const {sendMessage, lastMessage, readyState} = useWebSocket(webSocketUrl);
  

  useEffect(() => {
    if (lastMessage) {
      if (lastMessage.data === 'ping') {
        sendMessage('pong');
        return;
      }
      const data = JSON.parse(lastMessage.data)
      if (data.tenMostRecentTransactions) {
        setTransactions(data.transactionsPerSecond)
        setMostRecentTransactions(data.tenMostRecentTransactions);
        
      }
      else {
        setUsers({...data});
      }
    }
  }, [lastMessage])

  const handleClick = () => {
    if (simulatorState === STOP) {
      sendMessage("start")
    }
    else {
      sendMessage("stop");
    }
    setSimulatorState((prev) => {
      if (prev === STOP) {
        return START;
      }
      return STOP;
    })
  }

  const renderTransactionCount = () => {
    return <h2>
      {transactions}
    </h2>
  }
  

  return (
    <>
      <div style={{width : "70%" , height: "100%", margin : "0 auto", marginTop : 50, flex : 1}}>
        <Button style={{marginBottom : 20}} onClick={handleClick}>
          {simulatorState === STOP && "Start transaction simulator"}
          {simulatorState === START && "Stop transaction simulator"}
        </Button>
        <div>
          Currently processing {renderTransactionCount()} transactions per second
        </div>
        <Flex justify='space-between' style={{marginTop: 20}}>
          <TransactionGrid mostRecentTransactions={mostRecentTransactions}/>
          <UsersGrid users={users}/>
        </Flex>
      </div>
      <div style={{minHeight : 50}}>
        <span>
          Made By: Jun Shern Lim | 
          <a style={{paddingLeft : 5}} href="https://github.com/Jun0610/payments-simulator">
            See the code here
          </a>
        </span>
      </div>

    </>

  )
}
 
export default App
