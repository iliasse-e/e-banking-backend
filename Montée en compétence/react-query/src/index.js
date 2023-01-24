/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import ReactDOM from "react-dom";
import { TableTemplate } from "simple-react-data-table-component";


export default function App() {
  const columns = [
    { title: 'First Name', data: 'firstName' },
    { title: 'Last Name', data: 'lastName' },
    { title: 'Street', data: 'street' },
    { title: 'City', data: 'city' },
    { title: 'State', data: 'state' },
    { title: 'Zip Code', data: 'zipCode' },
  ];
  
  const data = [
    {
      firstName: 'Arman',
      lastName: 'Tsarukyan',
      department: 'Human',
      street: 'Gumri avenue 55',
      city: 'Erevan',
      state: 'Armenia',
      zipCode: 29005,
    },
    {
      firstName: 'Islam',
      lastName: 'Makhachev',
      department: 'Human',
      street: 'Grozny street 111',
      city: 'Makhachkala',
      state: 'Russia',
      zipCode: 52005,
    }
  ];
  
  const color = 'rgb(255, 174, 75)'
  return (
    <div>
      <TableTemplate columns={columns} dataInput={data} color={color} />
    </div>
  )
}


const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);
