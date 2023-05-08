Feature: Location Finder

  Scenario Outline: Location scenario with different Postcode
    Given user navigates to url
    When user clicks on BranchFinder icon
    And User fills the Location finder with sheetname "<Sheetname>" and rownumber <RowNumber>
    And  User clicks on the Branch location Finder button
    Then User able to fetch the Phone Number of nearest Branch and get displayed
    Examples:
      | Sheetname | RowNumber |
      | Sheet1    | 0         |
      | Sheet1    | 1         |
      | Sheet1    | 2         |
      | Sheet1    | 3         |
      | Sheet1    | 4         |
      | Sheet1    | 5         |
      | Sheet1    | 6         |
      | Sheet1    | 7         |


