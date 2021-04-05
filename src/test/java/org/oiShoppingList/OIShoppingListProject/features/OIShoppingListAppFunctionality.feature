#Author: Sourabh Baya
Feature: OIShoppingList Application Functionality
  To verify Shopping List Functionality

  @ShoppingListApp
  Scenario: TC1_Add item list in Shopping List Application
  To validate whether user is able add product in shoppinglist application.

    Given the user has Launched "Shopping List" application in "shoppingApplication" page.
    And the user clicks on "Layout with Add item" button in "shoppingApplication" page.
    And the user verifies whether "My Shopping List" tab is "present" in "shoppingApplication" page. (Continue on Failure)
    When the user clicks on "Open Navigation Drawer" button in "shoppingApplication" page.
    And the user clicks on "New List" button in "shoppingApplication" page.
    And the user verifies whether "New Shopping List Title" tab is "present" in "shoppingApplication" page. (Continue on Failure)
    And the user enters "ShoppingList name" in "shoppingApplication" page.
    When the user clicks on "OK" button in "shoppingApplication" page.
    And the user enters "item name" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user hides the keyboard if present and then clicks on "Open Navigation Drawer" button in "shoppingApplication" page.
    And the user clicks on "New List" button in "shoppingApplication" page.
    And the user enters "ShoppingList name1" in "shoppingApplication" page.
    When the user clicks on "OK" button in "shoppingApplication" page.
    And the user enters "item name1" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user clicks on "More Option" button in "shoppingApplication" page.
    And the user clicks on "Delete List" button in "shoppingApplication" page.
    And the user verifies whether "Delete popUp Title" tab is "present" in "shoppingApplication" page. (Continue on Failure)
    And the user clicks on "OK" button in "shoppingApplication" page.


  @ShoppingListApp1
  Scenario: TC2_Sorting of item list in Shopping list application
  To validate whether user is able sort an item from shopping list application

    Given the user has Launched "Shopping List" application in "shoppingApplication" page.
    And the user clicks on "Layout with Add item" button in "shoppingApplication" page.
    And the user verifies whether "My Shopping List" tab is "present" in "shoppingApplication" page. (Continue on Failure)
    When the user clicks on "Open Navigation Drawer" button in "shoppingApplication" page.
    And the user clicks on "New List" button in "shoppingApplication" page.
    And the user verifies whether "New Shopping List Title" tab is "present" in "shoppingApplication" page. (Continue on Failure)
    And the user enters "ShoppingList name" in "shoppingApplication" page.
    When the user clicks on "OK" button in "shoppingApplication" page.
    And the user enters "item name" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user enters "item name1" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user enters "item name2" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user enters "item name3" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user enters "item name4" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user enters "item name5" in "shoppingApplication" page.
    And the user clicks on "ADD" button in "shoppingApplication" page.
    And the user hides the keyboard if present and then clicks on "More Option" button in "shoppingApplication" page.
    And the user clicks on "Settings" button in "shoppingApplication" page.
    And the user verifies whether "Settings" tab is "present" in "shoppingApplication" page. (Continue on Failure)
    And the user clicks on "Sort Order" button in "shoppingApplication" page.
    And the user clicks on "Alphabetical order" button in "shoppingApplication" page.
    And the user navigate back to previous page.
    And the user verifies sorting of "item list" in "shoppingApplication" page.