# volunteering-app
Group assignment for Mobile Application Development (WIX3004)

1. Make sure you register your SSH on github (https://docs.github.com/en/free-pro-team@latest/github/authenticating-to-github/adding-a-new-ssh-key-to-your-github-account)
2. Clone this repository using github desktop. (File -> Clone Repository -> Url -> enter URL: git@github.com:sbrnmrzk/volunteering-app.git
3. Import this project into Android Studio

## When making changes
1. Create a branch, name it related to your changes
2. Checkout to branch.
3. Make your code changes
4. Done with changes: git add, git commit, git push
5. Create pull request, add me as a reviewer.

## Drafts can be seen here:
https://www.figma.com/file/Lc05pH5sNop06I4L5ic3XV/MAD-proposal-Copy

From here, boleh download images if nak. 

## Tasks assigned: 
- User Authentication and profile - Amin
- Homepage, event details - Sabrina
- History, notifications - Bobby
- Account page and rewards - Ifa
- Create, edit, manage events - Fariz
  
### Tips:
- Fariz point out yang Fragment tu kalau nak navigate to activity, letak method navigation using intent kat activity where the fragment is located.
For example:

1. I create new activity called NewActivity
2. Add method NavigateToNewActivity to the activity where the fragment is, Homepage.java

`
    public boolean navigateToNewActivity(View view){
        Intent activity = new Intent (this, NewActivity.class);
        startActivityForResult(activity, 1);
        return true;
    }
`

3. I add a button in the AccountFragment fragment. Button onclick property should be written to navigateToNewActivity


I'm here to test.