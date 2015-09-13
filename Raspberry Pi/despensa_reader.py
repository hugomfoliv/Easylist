from SimpleCV import Camera, Image
import sys
import subprocess
import requests

url_addItem = "http://178.62.25.30:4000/AddItem?"
url_removeItem = "http://178.62.25.30:4000/RemoveItem?"

def read_QRCode():
	#subprocess.call(['raspistill -w 640 -h 480 -t 5000 -o qrcode.png'], shell=True)
	img = Image('qrcode.png')
	qrcode = img.findBarcode()
	
	if qrcode:
		data_qrcode = str(qrcode[0].data)
		data_qrcode = data_qrcode.split(';')
		for item in data_qrcode:
			itemSplitted = item.split(',')
			status = inserir_item(itemSplitted[0],itemSplitted[1],itemSplitted[2])
			if status == 200:
				print "O seguinte produto for adicionado: " + itemSplitted[1]
			else:
				print "Erro na comunicacao com o servidor!"

	else:
		print "Nenhum produto detetado"

def read_Barcode():
	dataBarcode = ''
	fp = open('/dev/hidraw4', 'rb')

	nProdutosRetirados = 0
	nProdutos = raw_input('Quantos produtos vai retirar?: ')
	nProdutos = int(nProdutos)

	while nProdutosRetirados < nProdutos:
   		while len(dataBarcode) != 13:
   			buffer = fp.read(8)
   			for c in buffer:
   				if ord(c) > 0 and ord(c) != 40:
   					if ord(c) >= 30 and ord(c) <= 38:
   						dataBarcode += str(int(ord(c)) - 29)
   					else:
   						dataBarcode += "0"
		
		status = remover_item(dataBarcode)
		if status == 200:
			print "O produto com o codigo de barras " + dataBarcode + " foi removido!"
		else:
			print "Erro na comunicacao com o servidor!"
		
		dataBarcode = ''

def inserir_item(codigo, nome, val):
	request = url_addItem + "nome=" + nome + "&codigo_barras=" + codigo + "&validade=" + val
	r = requests.get(request)
	return r.status_code

def remover_item(codigo):
	request = url_removeItem + "codigo_barras=" + codigo
	r = requests.get(request)
	return r.status_code

#read_QRCode()
#read_Barcode()
