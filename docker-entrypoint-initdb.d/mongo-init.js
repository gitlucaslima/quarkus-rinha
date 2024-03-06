db = db.getSiblingDB('rinha')

db.createCollection("clientes")

db.clientes.insertMany([
  { idCliente: 1, nome: "o barato sai caro", limite: 1000 * 100, saldo: 0 },
  { idCliente: 2, nome: "zan corp ltda", limite: 800 * 100, saldo: 0 },
  { idCliente: 3, nome: "les cruders", limite: 10000 * 100, saldo: 0 },
  { idCliente: 4, nome: "padaria joia de cocaia", limite: 100000 * 100, saldo: 0 },
  { idCliente: 5, nome: "kid mais", limite: 5000 * 100, saldo: 0 }
])

db.createCollection("transacoes")