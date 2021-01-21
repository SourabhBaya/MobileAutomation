#Author: Sourabh Baya
Feature: Amazon Application Functionality
  To verify Amazon Select Product Functionality

  @AmazonTest
  Scenario: TC1_Select Product From Amazon App
  To validate whether user is able to select product from Amazon App

    Given the user has Launched "Amazon" application in "AmazonLogin" page.
    Given the user may get a "Select Your Language" link in "AmazonLogin" page.
    When the user clicks on "AlreadyCustomer" button in "AmazonLogin" page.
    And the user enters "Username" in "AmazonLogin" page.
    When the user clicks on "Continue" button in "AmazonLogin" page.
    Given the user may get a "Show Password" link in "AmazonLogin" page.
    And the user enters "Password" in "AmazonLogin" page.
    And the user hides the keyboard if present and then clicks on "Login" button in "AmazonLogin" page.
    And the user clicks on "Search" button in "Amazon Home" page.
    And the user enters "Product Detail to Search" in "Amazon Home" page.
    And the user clicks on "Product details" button in "Amazon Home" page.
    And the user scroll till "Sony Bravia 164 cm" button in "Amazon Home" page.
    And the user scroll till "Add to Cart" button in "Product home" page.
    And the user verifies whether "Proceed to checkout" field is "present" in "Product home" page.
    And the user clicks on "Cart" button in "Product home" page.
    And the user verifies whether "Price" field is "present" in "Product home" page.
    And the user verifies whether "Proceed to buy" field is "present" in "Product home" page.
    And the user clicks on "Proceed to buy" button in "Product home" page.
    And the user verifies whether "Select a delivery address" field is "present" in "Product home" page.
    And  the user waits till the page gets loaded.
