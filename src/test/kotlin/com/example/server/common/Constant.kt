package com.example.server.common

object Constant {
    const val EMAIL = "usman@gmail.com"
    private const val SEARCH_RESULTS = "   \"results\" : [\n" +
            "      {\n" +
            "         \"business_status\" : \"OPERATIONAL\",\n" +
            "         \"geometry\" : {\"location\" : {\n" +
            "               \"lat\" : 31.539462,\n" +
            "               \"lng\" : 74.35047899999999\n" +
            "            }},\n" +
            "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/lodging-71.png\",\n" +
            "         \"icon_background_color\" : \"#909CE1\",\n" +
            "         \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/hotel_pinlet\",\n" +
            "         \"name\" : \"The Residency Hotel\",\n" +
            "         \"photos\" : [\n" +
            "            {\n" +
            "               \"height\" : 1362,\n" +
            "               \"photo_reference\" : \"123456789\",\n" +
            "               \"width\" : 2048\n" +
            "            }\n" +
            "         ],\n" +
            "         \"place_id\" : \"786\",\n" +
            "         \"rating\" : 4.2,\n" +
            "         \"reference\" : \"786\",\n" +
            "         \"scope\" : \"GOOGLE\",\n" +
            "         \"types\" : [\n" +
            "            \"cafe\",\n" +
            "            \"lodging\",\n" +
            "            \"restaurant\",\n" +
            "            \"food\",\n" +
            "            \"point_of_interest\",\n" +
            "            \"establishment\"\n" +
            "         ],\n" +
            "         \"user_ratings_total\" : 978,\n" +
            "         \"vicinity\" : \"39-A Zafar Ali Road, Gulberg V, Lahore\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"status\" : \"OK\"\n"

            const val SEARCH_RESPONSE = "{\n" +
            "   \"html_attributions\" : [],\n" +
            SEARCH_RESULTS +
            "}\n"
    const val SEARCH_RESPONSE_2 = "{\n" +
            "   \"html_attributions\" : [],\n" +
            "   \"next_page_token\" : 123,\n" +
            SEARCH_RESULTS +
            "}\n"
    const val SECRET_RESPONSE_GOOGLE_MAPS = "{\"googleMapsKey\":\"123\",\"autocompleteRadius\":\"5000.0\",\"autocompleteTypes\":\"restaurant\",\"autocompleteRegionType\":\"(geocode)\",\"nearbySearchRadius\":\"5000.0\",\"nearbySearchType\":\"restaurant\"}"
    const val INTERNATIONALIZATION = "{\n" +
            "\"US\": {\n" +
            "\"detail_title\": \"Top 10 best restaurants in\",\n" +
            "\"detail_main_para\": \"From the Grumpeat community Latest update on Wednesday March 9 2022\",\n" +
            "\"detail_description_section1_part1\": \"March 2022: Looking for the best things to do in\",\n" +
            "\"detail_description_section1_part2\": \"in addition to dining? See our recommendations here, and take a peek at the city s best restaurants!\",\n" +
            "\"detail_description_section2_part1\": \"Choosing a favourite restaurant in\",\n" +
            "\"detail_description_section2_part2\": \"is a joyful task with myriad possibilities depending on the occasion, mood and even the time of year. Your favourite dive, fine dining destination and any night type of place might all occupy top spots on your personal best list in spite of their disparate qualities.\",\n" +
            "\"detail_description_section3_part1\": \"Our list of\",\n" +
            "\"detail_description_section3_part2\": \"10 best restaurants is the same, spanning each of those categories and more to comprise a catalogue of all the places we wish we were at right now. They don’t have to be the newest or the most famous (though some are)\",\n" +
            "\"featured_countries\": \"Featured countries\",\n" +
            "\"popular_searches\": \"Popular searches\",\n" +
            "\"neighbourhood\": \"Neighbourhood\",\n" +
            "\"header_left_link\": \"Get the app\",\n" +
            "\"header_left_title_link\": \"For business\",\n" +
            "\"logo_title\": \"Grumpeat\",\n" +
            "\"sign_in\": \"Sign in\",\n" +
            "\"sign_up\": \"Sign up\",\n" +
            "\"home_title\": \"Welcome to\",\n" +
            "\"home_title_city\": \"Istanbul\",\n" +
            "\"home_title_last\": \" foodie community\",\n" +
            "\"home_paragraph_one\": \"Find the best places to eat and drink in any city worldwide.\",\n" +
            "\"home_paragraph_two\": \"Access 25 000+ recommendations from local experts\",\n" +
            "\"our_goal_title\": \"Our goal\",\n" +
            "\"goal_card_one_title\": \"No disappointments\",\n" +
            "\"goal_card_one_paragraph\": \"To have nobody be disappointed when trying a new restaurant again\",\n" +
            "\"goal_card_two_title\": \"Best recommendations\",\n" +
            "\"goal_card_two_paragraph\": \"Reward the best restaurants with our community unbiased recommendations.\",\n" +
            "\"trending_restaurants_title\": \"Trending restaurants\",\n" +
            "\"tab_section_title\": \"Explore the best food & drink for your taste nearby\",\n" +
            "\"tab_section_title_btn_one\": \"Popular cuisines types\",\n" +
            "\"tab_section_title_btn_two\": \"Popular restaurant types\",\n" +
            "\"footer_title\": \"Get the best recomendations to eat and drick in any city Download Grumpeat now!\",\n" +
            "\"footer_paragraph\": \"Try it, it’s free\",\n" +
            "\"footer_btn_one\": \"IOS\",\n" +
            "\"footer_btn_two\": \"Android\",\n" +
            "\"footer_link_one_paragraph\": \"Find the best places to eat and drink in any city worldwide\",\n" +
            "\"footer_link_two_title\": \"For enterprises\",\n" +
            "\"footer_link_two_para\": \"For business\",\n" +
            "\"footer_link_three_title\": \"More\",\n" +
            "\"footer_link_three_para_one\": \"Grumpeat for iOS\",\n" +
            "\"footer_link_three_para_two\": \"Grumpeat for Andoid\",\n" +
            "\"footer_link_three_para_three\": \"Community\",\n" +
            "\"footer_link_four_title\": \"Legal\",\n" +
            "\"footer_link_four_para_one\": \"Terms and Condition\",\n" +
            "\"footer_link_four_para_two\": \"Privacy Policy\",\n" +
            "\"footer_copy_right\": \"©2022 Grumpeat. All rights reserved\"\n" +
            "}\n" +
            "}"
    const val EMAIL_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "</head>\n" +
            "<body>\n" +
            "<p>Hello, Your confirmation code is</p>\n" +
            "<p>1234</p>\n" +
            "<p>. The confirmation code will be valid for 60 minutes. If you did not initiate this operation, you can ignore this email.\\n\\n Grumpeat Team\\n Automated message. Please do not reply. [Grumpeat] Email Confirmation</p>\n" +
            "</body>\n" +
            "</html>"


}