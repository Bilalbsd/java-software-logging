import { ZipkinExporter } from '@opentelemetry/exporter-zipkin';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { XMLHttpRequestInstrumentation } from '@opentelemetry/instrumentation-xml-http-request';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';
import { DocumentLoadInstrumentation } from '@opentelemetry/instrumentation-document-load';
import { SemanticResourceAttributes } from '@opentelemetry/semantic-conventions';
// import { ConsoleInstrumentation } from '@opentelemetry/instrumentation-console';
// import { HistoryInstrumentation } from '@opentelemetry/instrumentation-history';

import { Resource } from '@opentelemetry/resources';

import {
  SimpleSpanProcessor,
  WebTracerProvider,
} from '@opentelemetry/sdk-trace-web';

// Create the WebTracerProvider instance
const provider = new WebTracerProvider({
  resource: new Resource({
    [SemanticResourceAttributes.SERVICE_NAME]: 'frontend_service',
  }),
});

// Create a Zipkin exporter with the configuration
const zipkinExporter = new ZipkinExporter({
  serviceName: 'frontend-service',
  url: 'http://localhost:9411/api/v2/spans', // URL de votre backend Zipkin
});

// Add the SimpleSpanProcessor with the Zipkin exporter
provider.addSpanProcessor(new SimpleSpanProcessor(zipkinExporter));

// Register the provider with OpenTelemetry
provider.register();

// Register the instrumentations, including the XMLHttpRequestInstrumentation and other additional ones
registerInstrumentations({
  instrumentations: [
    new XMLHttpRequestInstrumentation(),
    new FetchInstrumentation(),
    new DocumentLoadInstrumentation(),
    // new ConsoleInstrumentation(),
    // new HistoryInstrumentation(),
  ],
});
