package sleuth.webmvc;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import reactor.util.context.Context;
import reactor.util.context.ContextRelay;

class MySubscriber implements Subscriber<Object>, ContextRelay {

	private final Span span;
	private final Subscriber<? super Object> subscriber;
	private final Context context;
	private final Tracer tracer;

	public MySubscriber(Span span, Subscriber<? super Object> subscriber, Context context,
			Tracer tracer) {
		this.span = span;
		this.subscriber = subscriber;
		this.tracer = tracer;
		this.context = context.put(Span.class, span);
	}

	@Override public void onSubscribe(Subscription subscription) {
		tracer.continueSpan(span);
		subscriber.onSubscribe(subscription);
	}

	@Override public void onNext(Object o) {
		subscriber.onNext(o);
	}

	@Override public void onError(Throwable throwable) {
		subscriber.onError(throwable);
		if (tracer.isTracing()) {
			tracer.detach(tracer.getCurrentSpan());
		}
	}

	@Override public void onComplete() {
		subscriber.onComplete();
		if (tracer.isTracing()) {
			tracer.detach(tracer.getCurrentSpan());
		}
	}

	@Override public Context currentContext() {
		return this.context;
	}
}