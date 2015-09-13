import requests

url_addItem = "http://178.62.25.30:4000/AddItem?"
url_removeItem = "http://178.62.25.30:4000/RemoveItem?"

def inserir_item(nome, codigo, val):
	request = url_addItem + "nome=" + nome + "&codigo_barras=" + codigo + "&validade=" + val
	r = requests.get(request)
	print r.status_code

def remover_item(codigo):
	request = url_removeItem + "codigo_barras=" + codigo
	r = requests.get(request)
	print r.status_code

#inserir_item("bananas","2222222222222","2020-05-05")
remover_item("2222222222222")
