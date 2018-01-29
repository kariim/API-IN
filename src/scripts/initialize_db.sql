
DELETE FROM public.authorized_api_keys_topics;
DELETE FROM public.authorized_api_keys;
DELETE FROM public.authorized_topics;

INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-defaut-metrics');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi2-dsc-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi2-fgai-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi2-mat-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi2-mmd-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi2-wrh-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-nucleaire-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-plateausdin-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-plateausint-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-ris-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-o2-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-thit-surveillance');
INSERT INTO public.authorized_topics(url_suffix) VALUES ('fr.edf.dsp.dlk.api.spring1-osi1-plateaurenouveau-surveillance');


INSERT INTO public.authorized_api_keys(client_key) VALUES ('test0001-0000-0000-0000-000000000000');


INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-defaut-metrics');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi2-dsc-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi2-fgai-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi2-mat-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi2-mmd-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi2-wrh-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-nucleaire-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-plateausdin-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-plateausint-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-ris-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-o2-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-thit-surveillance');
INSERT INTO public.authorized_api_keys_topics(client_key, url_suffix) VALUES ('test0001-0000-0000-0000-000000000000', 'fr.edf.dsp.dlk.api.spring1-osi1-plateaurenouveau-surveillance');