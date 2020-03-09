/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { MemberStateComponent } from '../../../../../../main/webapp/app/entities/member-state/member-state.component';
import { MemberStateService } from '../../../../../../main/webapp/app/entities/member-state/member-state.service';
import { MemberState } from '../../../../../../main/webapp/app/entities/member-state/member-state.model';

describe('Component Tests', () => {

    describe('MemberState Management Component', () => {
        let comp: MemberStateComponent;
        let fixture: ComponentFixture<MemberStateComponent>;
        let service: MemberStateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MemberStateComponent],
                providers: [
                    MemberStateService
                ]
            })
            .overrideTemplate(MemberStateComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MemberStateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MemberStateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MemberState(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.memberStates[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
