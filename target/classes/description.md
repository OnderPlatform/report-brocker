##GET /meters

**return**
`
[{ uuid: string;
  name: string;
  comment: string;
}, ...
]`


##GET /meters/relations

**return**

`[{
meterUuid: string,
sellerUuid: string
}, ...]`

##GET /reports/consumption
Отчет потребления за 24 часа. Интервалы 10 мин

**return**

`[{
time: long;
consumption: double
}, ...]`


##GET /reports/price
Отчет цены за 24 часа. Интервалы 10 мин

**return**

`[{
time: long;
amount: double
}, ...]`


##GET /reports/meters

Агрегированные данные за последние 24 часа

**return**

`[{uuid: string,
  price: double,
  saleKWh: double,
  saleTokens: double,
  purshaseKWh: double,
  purshaseTokens: double,
  updateTime: long,
}
]`


##Web sockets
Аггрегированные данные потребление за последний тик

**return**

`{
time: long,
instantConsumption: double,
instantPrice: double,
meters:
[{uuid: string,
  price: double,
  saleKWh: double,
  saleTokens: double,
  purshaseKWh: double,
  purshaseTokens: double,
  updateTime: long,
}]
}`