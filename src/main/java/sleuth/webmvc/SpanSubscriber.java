package sleuth.webmvc;

import java.util.concurrent.atomic.AtomicBoolean;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.context.Context;
import reactor.util.context.ContextRelay;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

class SpanSubscriber extends AtomicBoolean
		implements Subscriber<Object>, Subscription, ContextRelay {

	static final Logger log = Loggers.getLogger(SpanSubscriber.class);

	private final Span                       span;
	private final Span                       rootSpan;
	private final Subscriber<? super Object> subscriber;
	private final Context                    context;
	private final Tracer                     tracer;
	private       Subscription               s;

	SpanSubscriber(Subscriber<? super Object> subscriber, Context ctx, Tracer tracer) {
		this.subscriber = subscriber;
		this.tracer = tracer;

		Span root = ctx.getOrDefault(Span.class, tracer.getCurrentSpan());
		this.rootSpan = root != null && root.getSavedSpan() == null ? root : null;
		this.span = tracer.createSpan(subscriber.toString(), root);
		this.context = ctx.put(Span.class, this.span);
	}

	@Override public void onSubscribe(Subscription subscription) {
		this.s = subscription;
		tracer.continueSpan(span);
		subscriber.onSubscribe(this);
	}

	@Override
	public void request(long n) {
		tracer.continueSpan(span);
		s.request(n);
	}

	@Override
	public void cancel() {
		try {
			s.cancel();
		}
		finally {
			cleanup();
		}
	}

	@Override public void onNext(Object o) {
		subscriber.onNext(o);
	}

	@Override public void onError(Throwable throwable) {
		try {
			subscriber.onError(throwable);
		}
		finally {
			cleanup();
		}
	}

	@Override public void onComplete() {
		try {
			subscriber.onComplete();
		}
		finally {
			cleanup();
		}
	}

	void cleanup() {
		if (compareAndSet(false, true)) {
			if (tracer.getCurrentSpan() != span) {
				tracer.detach(tracer.getCurrentSpan());
				tracer.continueSpan(span);
			}
			tracer.close(span);
			if (rootSpan != null) {
				tracer.continueSpan(rootSpan);
				tracer.close(rootSpan);
			}
		}
	}

	@Override public Context currentContext() {
		return this.context;
	}
}