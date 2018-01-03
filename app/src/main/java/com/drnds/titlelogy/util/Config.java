package com.drnds.titlelogy.util;

import java.util.ArrayList;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class Config {

    public static Boolean NewOrderFlag = true;
    public static String Order_Filter = "";
    public static ArrayList selectedArrays = new ArrayList();
    public static int newoRderCount = 0;
    public static int Titlesearch = 0;
    public static int Titlesearchqc = 0;
    public static int Propertytyping = 0;
    public static int Propertytypingqc = 0;
    public static int Finalreview = 0;
    public static int Taxcerificate = 0;
    public static int Completedorders = 0;
    public static int Hold = 0;
    public static int clarification = 0;
    public static int cancelled = 0;

    //login url
    public static final String LOGIN_URL = "https://titlelogy.com/title_test_api/api/LoginUser/";
    //myaccount
    public static final String UPDATE_URL = "https://titlelogy.com/title_test_api/api/MyProfileEdit/";
    public static final String EDIT_URL = "https://titlelogy.com/title_test_api/api/MyProfile/";
    public static final String PASSWORD_URL = "https://titlelogy.com/title_test_api/api/Change_Password/";
    public static final String USERINFO_URL = "https://titlelogy.com/title_test_api/api/Edit_Client_User/";
    public static final String GETUSERINFO_URL = "https://titlelogy.com/title_test_api/api/EditUserProfileInfo/";
    //    createsubclient
    public static final String SUBCLIENT_URL = "https://titlelogy.com/title_test_api/api/Masters_Subclient/";
    public static final String DLTSUBCLIENT_URL = "https://titlelogy.com/title_test_api/api/Masters_Sub_Client_Delete/";
    public static final String EDITSUBCLIENT_URL = "https://titlelogy.com/title_test_api/api/Edit_Sub_Client/";

    //   state and county
    public static final String STATE_URL = "https://titlelogy.com/title_test_api/api/GetAllState/";
    public static final String COUNTY_URL = "https://titlelogy.com/title_test_api/api/GetAllCountyById/";

    //create User
    public static final String CREATEUSER_URL = "https://titlelogy.com/title_test_api/api/Create_Client_User/";
    public static final String VIEWUSER_URL = "https://titlelogy.com/title_test_api/api/View_Client_User/";
    public static final String EDITCLIENTUSER_URL = "https://titlelogy.com/title_test_api/api/Edit_Client_User/";
    public static final String DELETECLIENTUSER_URL = "https://titlelogy.com/title_test_api/api/Delete_Client_User/";

    //view vendor
    public static final String VIEWVENDOR_URL = "https://titlelogy.com/title_test_api/api/View_Client_Vendor/";

    //    Scoreboard fragment
    public static final String SCOREBOARD_URL = "https://titlelogy.com/title_test_api/api/Score_Board/";
    //    public static String GRIDVIEW_URL = "https://titlelogy.com/Title_api/api/View_Orders/";
    public static String  POSTSCORE = "https://titlelogy.com/title_test_api/api/View_Orders/";
    //    Orderqueue fragment
    public static String ORDERQUEUE_URL = "https://titlelogy.com/title_test_api/api/Order_Queue/";
    //    product type
    public static String PRODUCTTYPE_URL = "https://titlelogy.com/title_test_api/api/Client_Masters/1";
    //    ordertask
    public static String ORDERTASK_URL = "https://titlelogy.com/title_test_api/api/Client_Masters/2";
    //    orderstatus
    public static String ORDERSTATUS_URL = "https://titlelogy.com/title_test_api/api/Client_Masters/3";
    //    orderpriority
    public static String ORDERPRIORITY_URL = "https://titlelogy.com/title_test_api/api/Client_Masters/4";
    //    countytype
    public static String COUNTYTYPE_URL = "https://titlelogy.com/title_test_api/api/Client_Masters/5";
    //    subclient
    public static String SUBCLIENTORDER_URL = "https://titlelogy.com/title_test_api/api/Get_Sub_Client/";
    // document spinner
    public static String DOCUMENT_URL = "https://titlelogy.com/title_test_api/api/Client_Masters/6";
    //    date
    public static String DATE_URL = "https://titlelogy.com/title_test_api/api/Get_Pst_Date/";
    //createorder
    public static String CREATEORDER_URL = "https://titlelogy.com/title_test_api/api/Create_Order/";

    //    orderdetails
    public static String ORDERDETAILS_URL = "https://titlelogy.com/title_test_api/api/View_Oder_Details/";

    // getAllCountyType
    public static String GETTIRE_URL = "https://titlelogy.com/title_test_api/api/Get_County_Type_State_County/";

    //    Editordersubmit
    public static String EDITORDERSUBMIT_URL = "https://titlelogy.com/title_test_api/api/Edit_Order/";
    //    DashBoardFragment
    public static String DASHBOARD_URL = "https://titlelogy.com/title_test_api/api/Dashboard/";
    //    viewDocuments
    public static String VIEWDOCUMENTS_URL = "https://titlelogy.com/title_test_api/api/ViewDocuments/";
    //    upload documents
    public static String UPLOADDOCUMENT_URL = "https://titlelogy.com/title_test_api/api/UploadDocument/";
    //    Report url
    public static String REPORT_URL = "https://titlelogy.com/title_test_api/api/Production_Report/";
    //    Search url
    public static String SEARCH_URL = "https://titlelogy.com/title_test_api/api/Search_Order/";


    //VENDOR URLS

    //    vendorlogin
    public static final String VENDORLOGIN_URL = "https://titlelogy.com/title_test_api/api/VednorLogin/";
    //vendor scoreboard url
    public static final String VENDORSCOREBOARD_URL = "https://titlelogy.com/title_test_api/api/ScoreBoard/";
    //Dashboard
    public static final String VENDORDASHBOARD_URL = "https://titlelogy.com/title_test_api/api/VendorDashBoard/";
    //    venderorderqueue
    public static  String VENDORORDERQUEUE_URL = "https://titlelogy.com/title_test_api/api/MyOrders/";
    //    venderorderdetails
    public static  String VENDORORDERDETAILS_URL = "https://titlelogy.com/title_test_api/api/View_MyOrders/";
    //    vendereditsubmit
    public static  String VENDOREDITORDERSUBMIT_URL = "https://titlelogy.com/title_test_api/api/Edit_ViewMyOrders/";
    //    vendersubclient
    public static final String VENDORSUBCLIENT_URL = "https://titlelogy.com/title_test_api/api/Ven_SubClient/";
    //    Editvendersubclient
    public static final String EDITVENDORSUBCLIENT_URL = "https://titlelogy.com/title_test_api/api/Edit_ViewSubClient/";
    //    vendorclient
    public static final String VENDORCLIENT_URL = "https://titlelogy.com/title_test_api/api/View_Clients/";
    //    editvendorclient
    public static final String EDITVENDORCLIENT_URL = "https://titlelogy.com/title_test_api/api/Edit_ViewClient/";
    //    Vendor Report
    public static  String VENDORREPORT_URL = "https://titlelogy.com/title_test_api/api/Ven_ProductionReport/";
    //    myaccount vendor
    public static final String VENDOR_PASSWORD_URL = "https://titlelogy.com/title_test_api/api/ExternalClientVendorUserChangePassword/";
    public static final String VENDOR_EDIT_URL = " https://titlelogy.com/title_test_api/api/EditVendorMyProfile/";
    public static final String VENDOR_GET_URL = "https://titlelogy.com/title_test_api/api/VendorMyProfile/";
    public static final String VENDOREDITUSERINFO_URL = "https://titlelogy.com/title_test_api/api/Edit_UserProfileInfo/";
    public static final String VENDORGETUSERINFO_URL = "https://titlelogy.com/title_test_api/api/UserProfileInfo/";
    //    accept orders
    public static final String ACCEPT_ORDERS = "https://titlelogy.com/title_test_api/api/Accept_Orders/";

    //   Vendor search
    public static String VENDOR_SEARCH_URL = "https://titlelogy.com/title_test_api/api/OrderSearch/";
    //token
    public static String VENDOR_DOCUMENTS = "https://titlelogy.com/title_test_api/api/Ven_View_Document/";
    public static final String TOKEN_REQUEST = "";
    public static String CLIIENT_SPINNER = "https://titlelogy.com/title_test_api/api/Get_Client/";
    public static String SUBCLIIENT_SPINNER = "https://titlelogy.com/title_test_api/api//Get_Subclient_For_Report/";
    public static String  urlJsonObj = "https://titlelogy.com/title_test_api/api/View_ScoreboardOrders/";
    public static String  VEN_DOCUPLOAD = "https://titlelogy.com/title_test_api/api/DocumentUpload/Uplodfile";
    public static String  CLIENTORDER_DOCUPLOAD = "https://titlelogy.com/title_test_api/api/DocumentUpload/Uplodfile/";
}






















