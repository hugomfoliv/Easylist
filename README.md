# Easylist
## PSC15 (Porto Summer of Code 2015) 3rd place winners

EasyList is an app which got third place in the competition Porto Summer of Code. With this app our main goal was to make the user’s every day life easier in one of the most important tasks that we face at least every week. 
 This app will have your pantry always updated, you can also create your shopping list using your voice. For example if you say rice the app will go to the supermarket’s database get the name and the price of the product. In addition you can also create a list for your most frequently boughts goods and when they are missing from your pantry they will be automatically added to your new shopping list. It also includes feautures such as a menu to see your stock (what you have in your pantry) and the expiration date for your products. If one of them is outdated the app will warn you. 
Easy List also works with QR codes. However, this requires Raspeberry Pi. The supermarket will give you a QR code when you get home you validate it using Pi and your stock will be updated. To remove an item you pass its bar code through pi and it will be removed from the stock.

### Technologies:
- Node.js + SQL (server side + DB)
- Android + java + JSON (client side)
- Python (Raspberry)
