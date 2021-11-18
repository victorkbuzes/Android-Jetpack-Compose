package com.example.profilecard

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.profilecard.ui.theme.MyTheme
import com.example.profilecard.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme{
                UserApplication()

            }

        }
    }
}

@Composable
fun UserApplication(userProfiles: List<UserProfile> = userProfileList){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list") {
        composable("users_list") {
            UserListScreen(userProfiles, navController) }
        composable("user_details/{userId}",
            arguments = listOf(navArgument("userId"){
                type = NavType.IntType

            })
        ) { navBackStackEntry ->


            UserProfileDetailScreen(navBackStackEntry.arguments!!.getInt("userId"), navController ) }

    }

}

@Composable
fun UserListScreen(userProfiles: List<UserProfile>,navController: NavHostController?) {
    Scaffold(topBar = { AppBar(title = "User List",
        icon = Icons.Default.Home
    ) {}}) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {

            LazyColumn {
                items(userProfiles){  userProfile ->
                    ProfileCard(userProfile = userProfile){
                        navController?.navigate("user_details/${userProfile.id}")
                    }
                }
            }

        }
    }

}

@Composable
fun AppBar( title: String, icon: ImageVector ,iconClickAction: ()-> Unit){
    TopAppBar(
        navigationIcon = {
            Icon(imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable(onClick = {iconClickAction.invoke()})
            )
                         },
        title = { Text(title)}
            )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit){
    Card(modifier = Modifier
        .padding(top =8 .dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
        .fillMaxWidth()
        .wrapContentHeight(align = Alignment.Top)
        .clickable(onClick = { clickAction.invoke() }),
    elevation = 8.dp,
        backgroundColor = Color.White
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            ProfilePicture(userProfile.pictureUrl, userProfile.status, 72.dp )
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)

        }

    }
}


@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp){
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (onlineStatus)
            MaterialTheme.colors.lightGreen
        else Color.Red
        ),
        modifier = Modifier
            .padding(16.dp)
            .size(imageSize),
        elevation = 4.dp
    ){

        Image(
            painter = rememberImagePainter(
                data = pictureUrl,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = "Profile Picture Card",
            modifier = Modifier.size(72.dp)
        )

        }

}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        CompositionLocalProvider(
            LocalContentAlpha provides (
                    if (onlineStatus)
                        1f else ContentAlpha.medium)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.h5
            )
        }
        CompositionLocalProvider(LocalContentAlpha provides (ContentAlpha.medium)) {
            Text(
                text = if (onlineStatus)
                    "Active now"
                else "Offline",
                style = MaterialTheme.typography.body2
            )
        }
    }

}

@Composable
fun UserProfileDetailScreen(userId: Int, navController: NavHostController?){
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }
    Scaffold(topBar = { AppBar(title = " User Profile Details",
        icon = Icons.Default.ArrowBack
    ) {
        navController?.navigateUp()

    } }){
        Surface(
            modifier = Modifier.fillMaxSize()
        ){
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                ProfilePicture(userProfile.pictureUrl, userProfile.status , 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)

            }

        }

    }


}

@Preview(showBackground = true)
@Composable
fun UserProfileDetailsPreview() {
    MyTheme {
        UserProfileDetailScreen(userId = 0, null)
    }
}


@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    MyTheme {
        UserListScreen(userProfiles = userProfileList, null)
    }
}