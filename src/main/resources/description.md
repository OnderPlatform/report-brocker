##GET /api/meters

**return**
`
[{ id: string;
  name: string;
  comment: string;
}, ...
]`


##GET /api/meters/relations

**return**

`[{
meterUuid: string,
sellerUuid: string
}, ...]`

##GET /api/reports/consumption
Отчет потребления за 24 часа. Интервалы 10 мин

**return**

`[{
time: long,
value: double
}, ...]`


##GET /api/reports/price
Отчет цены за 24 часа. Интервалы 10 мин

**return**

`[{
time: long;
value: string
}, ...]`


##GET /api/reports/meters

Агрегированные данные за последние 24 часа

**return**

`[{id: string,
  updateTime: long,
  price: double,
  saleKWh: string,
  saleTokens: string,
  purshaseKWh: string,
  purshaseTokens: string
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
[{id: string,
    updateTime: long,
    price: double,
    saleKWh: string,
    saleTokens: string,
    purshaseKWh: string,
    purshaseTokens: string
}]
}`