# WalletDemo

Usage for wallet link submodule 
1. generate/update your ssh key of laptop and add it to github

2. add WalletLink as a submodule

`$ git submodule add git@github.com:CoinbaseWallet/walletlink-mobile.git`

3. init the submodule and update it, it will recursively fill another submodule in lib folder, 
not need to add another link as submodule

`$ git submodule update --init --recursive`

4. In android studio import module, select walletlink/android folder as module

5. It may have bug for `Kotlin plugin should be enabled before 'kotlin-kapt' ` 
just swap it with `apply plugin: 'kotlin-android` in `build.gradle` of walltlink
