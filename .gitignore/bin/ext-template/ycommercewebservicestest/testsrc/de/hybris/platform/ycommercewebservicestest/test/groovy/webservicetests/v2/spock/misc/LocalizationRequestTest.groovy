package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.misc

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_BAD_REQUEST
import static org.apache.http.HttpStatus.SC_OK

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest

import spock.lang.Unroll
import groovyx.net.http.HttpResponseDecorator

@ManualTest
@Unroll
class LocalizationRequestTest extends AbstractSpockFlowTest {
	private PRODUCT_CODE = 2053226
	private PRODUCT_DESCRIPTION_ENGLISH = "With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation"
	private PRODUCT_DESCRIPTION_GERMAN = "description_de"

	private LANGUAGE_ENGLISH = 'en'
	private LANGUAGE_GERMAN = 'de'
	private LANGUAGE_INVALID = 'abcde'
	private CURRENCY_USD = 'USD'
	private CURRENCY_JPY = 'JPY'
	private CURRENCY_INVALID = 'FGHIJ'

	def "Send a request with default currency and language parameters when request: #format"() {
		when: "a product is requested no currency or language parameter"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE,
				contentType: format,
				requestContentType: URLENC
				)

		then: "a product is returned with default language and currency"
		with(response) {
			status == SC_OK
			data.description == PRODUCT_DESCRIPTION_ENGLISH
			data.price.currencyIso == CURRENCY_USD
		}

		where:
		format << [JSON, XML]
	}

	def "Send a request with language parameter specified when request: #format"() {
		when: "a product is requested with language parameter specified"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE,
				contentType: format,
				query: ['lang': LANGUAGE_GERMAN],
				requestContentType: URLENC
				)

		then: "the product is returned with the description in the specified language"
		with(response) {
			status == SC_OK
			data.description == PRODUCT_DESCRIPTION_GERMAN
			data.price.currencyIso == CURRENCY_USD
		}

		where:
		format << [JSON, XML]
	}

	def "Send a request with currency parameter specified when request: #format"() {
		when: "a product is requested with currency parameter specified"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE,
				contentType: format,
				query: ['curr': CURRENCY_JPY],
				requestContentType: URLENC
				)

		then: "the product is returned with the price in the specified currency"
		with(response) {
			status == SC_OK
			data.description == PRODUCT_DESCRIPTION_ENGLISH
			data.price.currencyIso == CURRENCY_JPY
		}

		where:
		format << [JSON, XML]
	}

	def "Send a request with invalid language parameter specified when request: #format"() {
		when: "a product is requested with invalid language parameter specified"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE,
				contentType: format,
				query: ['lang': LANGUAGE_INVALID],
				requestContentType: URLENC
				)

		then: "an error is thrown"
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'UnsupportedLanguageError'
			data.errors[0].message == 'Language  ' + LANGUAGE_INVALID + ' is not supported'
		}

		where:
		format << [JSON, XML]
	}

	def "Send a request with invalid currency parameter specified when request: #format"() {
		when: "a product is requested with invalid currency parameter specified"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/products/' + PRODUCT_CODE,
				contentType: format,
				query: ['curr': CURRENCY_INVALID],
				requestContentType: URLENC
				)

		then: "an error is thrown"
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'UnsupportedCurrencyError'
			data.errors[0].message == 'Currency ' + CURRENCY_INVALID + ' is not supported'
		}

		where:
		format << [JSON, XML]
	}
}
