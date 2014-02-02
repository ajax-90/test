
Scenario: user choose products and make order
When the user opens the default page
And clicks on element by './/*[@id='indexform:menuTable:0:spinner']/a[1]/span/span'
And clicks on element by './/*[@id='indexform:menuTable:1:spinner']/a[1]/span/span'
And clicks on element by './/*[@id='indexform:menuTable_data']/tr[2]/td[7]/div/div[2]'
And clicks on element by './/*[@id='indexform:menuTable_data']/tr[1]/td[7]/div/div[2]'
And press 'Order' button
Then element with './/*[@id='orderedform:selectedItems_data']/tr[1]/td[3]/label' has text 'item2'
And element with './/*[@id='orderedform:selectedItems_data']/tr[2]/td[3]/label' has text 'item1'


Scenario: admin can see allorders
When the user fills 'orderedform:name' field with 'user1'
And press 'Done' button
Then element with './/*[@id='adminForm:adminTable_data']/tr[1]/td[2]' has text 'item1'
