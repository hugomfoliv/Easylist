from Tkinter  import *
from SimpleCV import Camera, Image
import sys
import subprocess
import requests

url_addItem = "http://178.62.25.30:4000/AddItem?"
url_removeItem = "http://178.62.25.30:4000/RemoveItem?"


class Application(Frame):

	def __init__(self, master):
		Frame.__init__(self,master)
		self.grid()
		self.create_widgets()

	def create_widgets(self):
		self.operacao =""
		self.inserir_lbl = Label(self, text="Inserir Produto")
		self.inserir_lbl.grid(row = 0, column =0, padx=5)
		self.inserir_btn= Button(self, text="Inserir", command=self.inserir_metodo)
		self.inserir_btn.grid(row=0, column =1, padx=6, pady=3)
		
		self.checkMethod = StringVar()
		self.checkMethod.set(1)
		self.checkQrcode = Radiobutton(self,text="QRcode",variable=self.checkMethod, value=1)
		self.checkQrcode.grid(row=0, column=2)
		self.checkQrcode = Radiobutton(self,text="Barcode",variable=self.checkMethod, value=2)
		self.checkQrcode.grid(row=0, column=3)

		self.retirar_lbl = Label(self, text="Retirar Produto")
		self.retirar_lbl.grid(row=1, column=0,padx=5)
		self.retirar_btn= Button(self, text="Retirar", command=self.remover_metodo)
		self.retirar_btn.grid(row=1, column=1, padx=6, pady=3)
		
		self.inserir_lbl = Label(self,text="Numero de Produtos")
		self.inserir_lbl.grid(row=2, column=0,columnspan=2)

		self.resposta_lbl = Label(self)
		self.resposta_lbl.grid(row=3, column=0, columnspan=4, pady=4)

		self.nProdutos = IntVar()
		self.nProdutos_text = Entry(self, textvariable=self.nProdutos)
		self.nProdutos_text.grid(row=2, column=2, columnspan=2,padx=6)
		
		sep= Frame(height=2, bd=1, relief=SUNKEN)
		sep.grid(row=4, column=0, columnspan=5)
		
		self.nome= StringVar()
		self.nome_lbl = Label(self, text = "Nome do Produto")
		self.nome_lbl.grid(row=5, column=0, columnspan=2)
		self.nome_text = Entry(self, textvariable=self.nome)
		self.nome_text.grid(row=5, column=2, columnspan=2)
		
		self.val = StringVar()
		self.val_lbl = Label(self, text = "Validade")
		self.val_lbl.grid(row=6, column=0, columnspan=2)
		self.val_text = Entry(self, textvariable=self.val)
		self.val_text.grid(row=6, column=2, columnspan=2)

	def inserir_metodo(self):
		self.operacao = "inserir"
		if self.checkMethod.get() == "1":
			self.read_QRCode()
		else:
			self.update_resposta()

	def remover_metodo(self):
		self.operacao = "remover"
		self.update_resposta()

	def update_resposta(self):
		if int(self.nProdutos.get()) <= 0:
			self.resposta_lbl.configure(text='Numero de produtos Invalido', fg='red')
		else:
			self.resposta_lbl['text'] = "Passe o(s) "+ str(self.nProdutos.get()) +" artigo(s) "
			self.read_Barcode()

	def read_QRCode(self):
		#subprocess.call(['raspistill -w 640 -h 480 -t 5000 -o qrcode.png'], shell=True)
		img = Image('qrcode.png')
		qrcode = img.findBarcode()
		
		if qrcode:
			data_qrcode = str(qrcode[0].data)
			data_qrcode = data_qrcode.split(';')
			for item in data_qrcode:
				self.itemSplitted = item.split(',')
				self.inserir_item_qrcode()
				if self.r.status_code == 200:
					self.resposta_lbl.configure(text= "O seguinte produto for adicionado: " + str(self.itemSplitted[1]), fg='green')
				else:
					self.resposta_lbl.configure(text= "Erro na comunicacao com o servidor!", fg='red')

		else:
			print "Nenhum produto detetado"

	def read_Barcode(self):
		print "Passe BARCODE"
		self.dataBarcode = ''
		fp = open('/dev/hidraw4', 'rb')

		self.nProdutosRetirados = 0
		nProd= self.nProdutos.get()

		while self.nProdutosRetirados < nProd:
			while len(self.dataBarcode) != 13:
				buffer = fp.read(8)
				for c in buffer:
					if ord(c) > 0 and ord(c) != 40:
						if ord(c) >= 30 and ord(c) <= 38:
							self.dataBarcode += str(int(ord(c)) - 29)
						else:
							self.dataBarcode += "0"
			
			if self.operacao == "inserir":
				self.inserir_item()
			else:
				self.remover_item()
			if self.r.status_code == 200:
				if self.operacao == "inserir":
					self.resposta_lbl.configure(text='Produto com barcode ' + self.dataBarcode + ' foi adicionado!', fg='green')
				else:
					self.resposta_lbl.configure(text='Produto com barcode ' + self.dataBarcode + ' foi removido!', fg='green')
	
				self.nProdutosRetirados +=1
			else:
				self.resposta_lbl.configure(text='Erro na comunicacao com o servidor!', fg='red')
			self.dataBarcode = ''

	def inserir_item(self):
		request = url_addItem + "nome=" + str(self.nome.get()) + "&codigo_barras=" + self.dataBarcode + "&validade=" + str(self.val.get())
		self.r = requests.get(request)
		#return r.status_code

	def inserir_item_qrcode(self):
		request = url_addItem + "nome=" + self.itemSplitted[1] + "&codigo_barras=" +self.itemSplitted[0] + "&validade=" + self.itemSplitted[2]
		self.r = requests.get(request)

	def remover_item(self):
		request = url_removeItem + "codigo_barras=" + self.dataBarcode
		self.r = requests.get(request)
		#return r.status_code

#create the window
root = Tk()

#modify root window
root.title("EasyList")
#root.geometry("600x100")

app = Application(root)

#loop
app.mainloop()
