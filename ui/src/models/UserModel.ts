export class UserModel {
    constructor(public email: string, public name: string, public password?: string, public id?: number, public token?: string, public authenticationProvider?: string, public zipCode?: number, public birthdate?: string, public image?: File, public imageUrl?: string, public imgBase64?: string,) {
    }

    static toFormDataFromPartial(data: Partial<UserModel>): FormData {
        const formData = new FormData();

        if (data.id !== undefined) formData.append('id', String(data.id));
        if (data.name !== undefined) formData.append('name', data.name);
        if (data.email !== undefined) formData.append('email', data.email);
        if (data.password !== undefined) formData.append('password', data.password);
        if (data.zipCode !== undefined) formData.append('zipCode', String(data.zipCode));
        if (data.birthdate !== undefined) formData.append('birthdate', data.birthdate);
        if (data.authenticationProvider !== undefined) formData.append('authenticationProvider', data.authenticationProvider);
        if (data.image !== undefined) formData.append('image', data.image);

        return formData;
    }

    toFormData(): FormData {
        return UserModel.toFormDataFromPartial(this);
    }

    toAuthenticationDto(): {
        email: string;
        password?: string | null;
        token?: string;
        authenticationProvider: string;
        name?: string;
        zipCode?: number,
        birthdate?: string
    } {
        return {
            email: this.email,
            password: this.password ?? null,
            token: this.token ?? "",
            authenticationProvider: this.authenticationProvider ?? 'LOCAL',
            name: this.name ?? "User",
            zipCode: this.zipCode ?? -1,
            birthdate: this.birthdate ?? "1970-01-01"
        };
    }
}
