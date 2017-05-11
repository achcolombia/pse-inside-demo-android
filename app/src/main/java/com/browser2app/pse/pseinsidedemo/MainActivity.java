package com.browser2app.pse.pseinsidedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.browser2app.khenshin.KhenshinConstants;
import com.browser2app.khenshin.activities.StartPaymentActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private static final int START_PAYMENT_REQUEST_CODE = 101;

	private EditText ecus;
	private EditText amount;
	private String authorizerId;
	private EditText subject;
	private EditText merchant;
	private String userTypeId;
	private EditText returnURL;
	private EditText payerEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ecus = (EditText) findViewById(R.id.ecus);
		amount = (EditText) findViewById(R.id.amount);
		Spinner authorizerSpinner = (Spinner) findViewById(R.id.authorizerId);
		ArrayList<Authorizer> authorizers = new ArrayList<>();
		authorizers.add(new Authorizer("1040", "BANCO AGRARIO"));
		authorizers.add(new Authorizer("1052", "BANCO AV VILLAS"));
		authorizers.add(new Authorizer("1013", "BANCO BBVA COLOMBIA S.A."));
		authorizers.add(new Authorizer("1032", "BANCO CAJA SOCIAL"));
		authorizers.add(new Authorizer("1019", "BANCO COLPATRIA"));
		authorizers.add(new Authorizer("1066", "BANCO COOPERATIVO COOPCENTRAL"));
		authorizers.add(new Authorizer("1006", "BANCO CORPBANCA S.A"));
		authorizers.add(new Authorizer("1051", "BANCO DAVIVIENDA"));
		authorizers.add(new Authorizer("1001", "BANCO DE BOGOTA"));
		authorizers.add(new Authorizer("1023", "BANCO DE OCCIDENTE"));
		authorizers.add(new Authorizer("1062", "BANCO FALABELLA"));
		authorizers.add(new Authorizer("1012", "BANCO GNB SUDAMERIS"));
		authorizers.add(new Authorizer("1060", "BANCO PICHINCHA S.A."));
		authorizers.add(new Authorizer("1002", "BANCO POPULAR"));
		authorizers.add(new Authorizer("1058", "BANCO PROCREDIT"));
		authorizers.add(new Authorizer("1007", "BANCOLOMBIA"));
		authorizers.add(new Authorizer("1061", "BANCOOMEVA S.A."));
		authorizers.add(new Authorizer("1009", "CITIBANK"));
		authorizers.add(new Authorizer("1014", "HELM BANK S.A."));
		authorizers.add(new Authorizer("1507", "NEQUI"));

		ArrayAdapter<Authorizer> authorizerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, authorizers);
		authorizerSpinner.setAdapter(authorizerAdapter);
		authorizerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				Authorizer authorizer = (Authorizer)adapterView.getSelectedItem();
				authorizerId = authorizer.getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		subject = (EditText) findViewById(R.id.subject);
		merchant = (EditText) findViewById(R.id.merchant);
		Spinner userTypeSpinner = (Spinner) findViewById(R.id.userType);
		ArrayList<UserType> userTypes = new ArrayList<>();
		userTypes.add(new UserType("0", "Persona Natural"));
		userTypes.add(new UserType("1", "Persona Jurídica"));
		ArrayAdapter<UserType> userTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
		userTypeSpinner.setAdapter(userTypeAdapter);
		userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				UserType userType = (UserType)adapterView.getSelectedItem();
				userTypeId = userType.getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		returnURL = (EditText) findViewById(R.id.returnURL);
		payerEmail = (EditText) findViewById(R.id.payerEmail);
	}


	public void doPay(View view) {
		Intent intent = new Intent(MainActivity.this, StartPaymentActivity.class);

		String automatonId = userTypeId + authorizerId;

		intent.putExtra(KhenshinConstants.EXTRA_AUTOMATON_ID, automatonId);
		Bundle params = new Bundle();

		params.putString("cus", ecus.getText().toString());
		params.putString("amount", amount.getText().toString() + ".00");
		params.putString("authorizerId", authorizerId);
		params.putString("subject", subject.getText().toString());
		params.putString("merchant", merchant.getText().toString());
		params.putString("cancelURL", returnURL.getText().toString());
		params.putString("paymentId", ecus.getText().toString());
		params.putString("userType", userTypeId);
		params.putString("returnURL", returnURL.getText().toString());
		params.putString("payerEmail", payerEmail.getText().toString());

		intent.putExtra(KhenshinConstants.EXTRA_AUTOMATON_PARAMETERS, params);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, START_PAYMENT_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == START_PAYMENT_REQUEST_CODE) {
			String exitUrl = data.getStringExtra(KhenshinConstants.EXTRA_INTENT_URL);
			if (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, "PAYMENT OK, exit url: " + exitUrl,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MainActivity.this, "PAYMENT FAILED, exit url: " + exitUrl,
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
