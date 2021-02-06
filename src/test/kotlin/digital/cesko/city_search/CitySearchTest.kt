package digital.cesko.city_search

import digital.cesko.AbstractSpringDatabaseTest
import net.javacrumbs.jsonunit.spring.jsonContent
import kotlin.test.Test

class CitySearchTest : AbstractSpringDatabaseTest() {

    @Test
    fun testCitySearch() {
        get("/api/v2/service/citysearch?query=jilove+u+prahy").andExpect {
            status { isOk }
            header {
                exists("Etag")
            }
            jsonContent {
                inPath("\$.[?(@.ico=='00241326')]").isNotNull()
            }
        }
    }

    @Test
    fun testSearchKnownCity() {
        get("/api/v2/service/citysearch?query=Cernosice").andExpect {
            status { isOk }
            jsonContent {
                inPath("\$.[?(@.ico=='00241326')]").isNotNull()
                node("[0]").isEqualTo(
                    """
                {
                   "adresaUradu":{
                      "adresniBod":"6506836",
                      "castObce":"Černošice",
                      "cisloDomovni":"259",
                      "kraj":"Středočeský",
                      "obecKod":"539139",
                      "obec":"Černošice",
                      "ulice":"Karlštejnská",
                      "castObce": "Černošice",
                      "cisloOrientacni": null,
                      "PSC":"25228"

                    },
                    "datovaSchrankaID":"u46bwy4",
                    "mail": [
                      "podatelna@mestocernosice.cz"
                    ],
                    "nazev":"MĚSTO ČERNOŠICE",
                    "hezkyNazev": "Černošice",
                    "souradnice":[
                      49.95886668901324,
                      14.320428348540815
                    ],
                    "zkratka":"CERNOSICE",
                    "urlCityVizor":"https://www.cityvizor.cz/cernosice/",
                    "urlZnak":"https://cityvizor-images.s3.eu-central-1.amazonaws.com/cernosice.png",
                    "pocetObyvatel":0,
                    "eDeskyID":"139",
                    "ICO":"00241121"
                }
                """
                )
            }
        }
    }

    @Test
    fun testSearchPraha() {
        get("/api/v2/service/citysearch?query=Praha 1").andExpect {
            status { isOk }
            jsonContent {
                node("[0].ICO").isString().isEqualTo("00063410")
            }
        }
    }

    @Test
    fun testGetKnownCities() {
        get("/api/v2/service/citysearch/knownCities").andExpect {
            status { isOk }
            jsonContent {
                isArray().hasSize(9)
                node("[0].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/cernosice/")
                node("[1].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/marianskelazne/")
                node("[2].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/nmnm/")
                node("[3].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/praha1/")
                node("[4].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/praha3/")
                node("[5].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/praha7/")
                node("[6].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/praha12/")
                node("[7].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/ub/")
                node("[8].uriCityVizor").isString().isEqualTo("https://www.cityvizor.cz/uvaly/")
            }
        }
    }
}
