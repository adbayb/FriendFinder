package dk.aau.mppss.friendfinder.controller;

/**
 * Created by adibayoub on 04/08/2015.
 */
public interface OnHttpAsyncTask {
    //Interface Listener permettant d'appeler des fonctions callback en dehors
    //de la classe HttpAsyncTask lorsque des fonctions de la classe HttpAsynTask sont appelees:
    void onHttpAsyncTaskCompleted(String result);
}